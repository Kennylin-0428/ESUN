<template>
  <div class="seating">
    <div v-for="(row, rowIndex) in seatingLayout" :key="rowIndex" class="row">
      <div
        v-for="(seat, seatIndex) in row"
        :key="seatIndex"
        class="seat-container"
      >
        <div
          :class="{
            seat: true,
            occupied:
              seat.status === 'occupied' &&
              !(
                selectedSeat &&
                selectedSeat.rowIndex === rowIndex &&
                selectedSeat.seatIndex === seatIndex &&
                selectedSeat.action === 'clear'
              ),
            available: seat.status === 'available',
            selected:
              seat.status === 'selected' ||
              (selectedSeat &&
                selectedSeat.rowIndex === rowIndex &&
                selectedSeat.seatIndex === seatIndex),
          }"
          @click="selectSeat(rowIndex, seatIndex)"
        >
          <template v-if="seat.status === 'occupied'">
            {{ seat.label }}
          </template>
          <template v-else>
            {{ seat.label }}
          </template>
        </div>
        <div
          v-if="
            selectedSeat &&
            selectedSeat.rowIndex === rowIndex &&
            selectedSeat.seatIndex === seatIndex &&
            selectedSeat.action === 'assign'
          "
          class="dropdown-container"
        >
          <select v-model="selectedEmployeeId" class="employee-dropdown">
            <option disabled value="">選擇員工</option>
            <option
              v-for="employee in employees"
              :key="employee.empId"
              :value="employee.empId"
            >
              {{ employee.name }} ({{ employee.empId }})
            </option>
          </select>
        </div>
      </div>
    </div>
    <div class="status-container">
      <div class="status">
        <div class="status-box available"></div>
        <div class="status-text">空位</div>
      </div>
      <div class="status">
        <div class="status-box occupied"></div>
        <div class="status-text">已佔用</div>
      </div>
      <div class="status">
        <div class="status-box selected"></div>
        <div class="status-text">請選擇</div>
      </div>
      <button
        class="submit-btn"
        @click="submitSeat"
        :disabled="
          !selectedSeat ||
          selectedSeat.action !== 'assign' ||
          !selectedEmployeeId
        "
      >
        送出
      </button>
      <button
        class="clear-btn"
        @click="clearSeat"
        :disabled="!selectedSeat || selectedSeat.action !== 'clear'"
      >
        清空座位
      </button>
    </div>
  </div>
</template>

<script>
import {
  getAllSeatingChart,
  assignSeatToEmployee,
  removeEmployeeFromSeat,
} from "../api/seatingChartApi";
import { getAllEmployee } from "../api/employeeApi";

export default {
  data() {
    return {
      seatingLayout: [],
      selectedSeat: null,
      employees: [],
      selectedEmployeeId: "",
    };
  },
  mounted() {
    this.loadSeatingChart();
    this.loadEmployees();
    document.addEventListener("click", this.handleClickOutside);
  },
  beforeUnmount() {
    document.removeEventListener("click", this.handleClickOutside);
  },
  methods: {
    async loadSeatingChart() {
      try {
        const response = await getAllSeatingChart();
        this.seatingLayout = this.formatSeatingData(response.data);
        console.log(this.seatingLayout);
      } catch (error) {
        console.error(error.message);
      }
    },
    async loadEmployees() {
      try {
        const response = await getAllEmployee();
        this.employees = response.data;
      } catch (error) {
        console.error(error.message);
      }
    },
    formatSeatingData(data) {
      const floors = {};
      const convertFloorLabel = (floorNo) => {
        const floorMap = {
          "1F": "1樓",
          "2F": "2樓",
          "3F": "3樓",
          "4F": "4樓",
        };
        return floorMap[floorNo] || floorNo;
      };
      data.forEach((seat) => {
        const floor = convertFloorLabel(seat.floorNo);
        if (!floors[floor]) {
          floors[floor] = [];
        }
        const seatLabel = seat.occupied
          ? `${floor}：座位 ${seat.seatNo} [員編:${seat.empId}]`
          : `${floor}：座位 ${seat.seatNo}`;

        floors[floor].push({
          label: seatLabel,
          status: seat.occupied ? "occupied" : "available",
          seatNo: seat.seatNo,
          floorNo: seat.floorNo,
          floorSeatSeq: seat.floorSeatSeq,
          empId: seat.empId,
        });
      });
      return Object.values(floors).map((seats) => seats);
    },

    selectSeat(rowIndex, seatIndex) {
      const seat = this.seatingLayout[rowIndex][seatIndex];
      if (this.selectedSeat) {
        const prev =
          this.seatingLayout[this.selectedSeat.rowIndex][
            this.selectedSeat.seatIndex
          ];
        if (prev.status === "selected") {
          prev.status = "available";
        }
      }
      if (seat.status === "available") {
        seat.status = "selected";
        this.selectedSeat = {
          rowIndex,
          seatIndex,
          seatData: seat,
          action: "assign",
        };
        this.selectedEmployeeId = "";
      } else if (seat.status === "occupied") {
        this.selectedSeat = {
          rowIndex,
          seatIndex,
          seatData: seat,
          action: "clear",
        };
      }
    },

    handleClickOutside(event) {
      const dropdown = document.querySelector(".dropdown-container");
      if (
        dropdown &&
        this.selectedSeat &&
        !dropdown.contains(event.target) &&
        !event.target.classList.contains("seat")
      ) {
        this.closeDropdown();
      }
    },

    closeDropdown() {
      if (this.selectedSeat) {
        const prev =
          this.seatingLayout[this.selectedSeat.rowIndex][
            this.selectedSeat.seatIndex
          ];
        if (prev.status === "selected") {
          prev.status = "available";
        }
        this.selectedSeat = null;
        this.selectedEmployeeId = "";
      }
    },

    async submitSeat() {
      if (
        !this.selectedSeat ||
        this.selectedSeat.action !== "assign" ||
        !this.selectedEmployeeId
      ) {
        alert("請選擇座位並分配員工！");
        return;
      }
      const { floorNo, seatNo } = this.selectedSeat.seatData;
      const payload = {
        empId: this.selectedEmployeeId,
        floorNo,
        seatNo,
      };
      try {
        const response = await assignSeatToEmployee(payload);
        alert("座位分配成功：" + response.data);
        this.loadSeatingChart();
        this.closeDropdown();
      } catch (error) {
        console.error(error.message);
        alert(error.message);
      }
    },

    async clearSeat() {
      if (!this.selectedSeat || this.selectedSeat.action !== "clear") {
        alert("請選擇一個已佔用的座位進行清空！");
        return;
      }
      const { floorNo, seatNo } = this.selectedSeat.seatData;
      const clearData = { floorNo, seatNo };
      try {
        const response = await removeEmployeeFromSeat(clearData);
        alert("座位已清空：" + response.data);
        this.loadSeatingChart();
        this.selectedSeat = null;
      } catch (error) {
        console.error(error.message);
        alert(error.message);
      }
    },
  },
};
</script>

<style scoped>
.seating {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.row {
  display: flex;
  gap: 5px;
  justify-content: space-between;
}

.seat-container {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}

.seat {
  width: 220px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  border: 1px solid #ccc;
  background-color: #d3d3d3;
  border-radius: 6px;
  text-align: center;
}

.occupied {
  background-color: red;
  color: white;
  cursor: not-allowed;
}

.available {
  background-color: rgb(205, 205, 205);
  color: rgb(0, 0, 0);
}

.selected {
  background-color: lightgreen;
  color: rgb(0, 0, 0);
}

.dropdown-container {
  position: absolute;
  width: 100%;
  height: 100%;
  top: -10%;
  left: 50%;
  transform: translateX(-50%);
  margin-top: 5px;
  z-index: 10;
}

.dropdown-container select {
  display: flex;
  width: 100%;
  height: 100%;
}

.employee-dropdown {
  width: 180px;
  padding: 5px;
  font-size: 14px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

.status-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  padding: 20px 0px;
}

.status {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.status-box {
  width: 30px;
  height: 30px;
  margin-right: 10px;
  border-radius: 5px;
}

.status-text {
  font-size: 14px;
  font-weight: bold;
}

.submit-btn {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: blue;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.submit-btn:hover {
  background-color: darkblue;
}

.submit-btn:disabled {
  background-color: gray;
  cursor: not-allowed;
}

.clear-btn {
  margin-top: 10px;
  padding: 10px 20px;
  background-color: orange;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.clear-btn:hover {
  background-color: darkorange;
}

.clear-btn:disabled {
  background-color: gray;
  cursor: not-allowed;
}
</style>
