<template>
  <div class="seating">
    <!-- 座位區域 -->
    <div v-for="(row, rowIndex) in seatingLayout" :key="rowIndex" class="row">
      <div
        v-for="(seat, seatIndex) in row"
        :key="seatIndex"
        class="seat-container"
      >
        <div
          :class="{
            seat: true,
            occupied: seat.status === 'occupied',
            available: seat.status === 'available',
            selected: seat.status === 'selected',
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
            selectedSeat.seatIndex === seatIndex
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
              {{ employee.empName }} ({{ employee.empId }})
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
        <div class="status-text">已選擇</div>
      </div>
      <button
        class="submit-btn"
        @click="submitSeat"
        :disabled="!selectedSeat || !selectedEmployeeId"
      >
        送出
      </button>
    </div>
  </div>
</template>

---

<script>
import { getAllSeatingChart, assignSeatToEmployee } from "../api/seatingChartApi";
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
  },
  methods: {
    async loadSeatingChart() {
      try {
        const response = await getAllSeatingChart();
        this.seatingLayout = this.formatSeatingData(response.data);
      } catch (error) {
        console.error(error.message);
      }
    },
    async loadEmployees() {
      try {
        const response = await getAllEmployee();
        this.employees = response.data; // 假設回傳 { empId, empName }
      } catch (error) {
        console.error(error.message);
      }
    },

    formatSeatingData(data) {
      const floors = {};
      data.forEach((seat) => {
        const floor = seat.floorNo;
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
      const selectedSeat = this.seatingLayout[rowIndex][seatIndex];

      if (selectedSeat.status === "available") {
        if (this.selectedSeat !== null) {
          const prevSeat = this.seatingLayout[this.selectedSeat.rowIndex][
            this.selectedSeat.seatIndex
          ];
          prevSeat.status = "available";
        }
        selectedSeat.status = "selected";
        this.selectedSeat = { rowIndex, seatIndex, seatData: selectedSeat };
        this.selectedEmployeeId = "";
      }
    },

    async submitSeat() {
      if (!this.selectedSeat || !this.selectedEmployeeId) {
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
        this.selectedSeat = null;
        this.selectedEmployeeId = "";
      } catch (error) {
        console.error(error.message);
        alert("座位分配失敗：" + error.message);
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
  width: 200px;
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
}

.selected {
  background-color: lightgreen;
}

.dropdown-container {
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  margin-top: 5px;
  z-index: 10;
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
</style>
