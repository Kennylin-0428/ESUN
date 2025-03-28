<template>
  <div class="seating">
    <div v-for="(row, rowIndex) in seatingLayout" :key="rowIndex" class="row">
      <div
        v-for="(seat, seatIndex) in row"
        :key="seatIndex"
        :class="{
          'seat': true,
          'occupied': seat.status === 'occupied',
          'available': seat.status === 'available',
          'selected': seat.status === 'selected',
        }"
        @click="selectSeat(rowIndex, seatIndex)"
      >
        {{ seat.label }}
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
      <button class="submit-btn">送出</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      seatingLayout: [
        [
          { label: '1樓：座位1', status: 'occupied' },
          { label: '1樓：座位2', status: 'occupied' },
          { label: '1樓：座位3', status: 'occupied' },
          { label: '1樓：座位4', status: 'occupied' },
        ],
        [
          { label: '2樓：座位1', status: 'available' },
          { label: '2樓：座位2', status: 'available' },
          { label: '2樓：座位3', status: 'available' },
          { label: '2樓：座位4', status: 'available' },
        ],
        [
          { label: '3樓：座位1', status: 'occupied' },
          { label: '3樓：座位2', status: 'occupied' },
          { label: '3樓：座位3', status: 'occupied' },
          { label: '3樓：座位4', status: 'occupied' },
        ],
        [
          { label: '4樓：座位1', status: 'available' },
          { label: '4樓：座位2', status: 'available' },
          { label: '4樓：座位3', status: 'available' },
          { label: '4樓：座位4', status: 'available' },
        ],
      ],
      selectedSeat: null, // Store the index of the selected seat
    };
  },
  methods: {
    selectSeat(rowIndex, seatIndex) {
      const selectedSeat = this.seatingLayout[rowIndex][seatIndex];
      
      // If the seat is available, change its status to selected
      if (selectedSeat.status === 'available') {
        // If a seat was already selected, reset it to available
        if (this.selectedSeat !== null) {
          const prevSelectedSeat = this.selectedSeat;
          this.seatingLayout[prevSelectedSeat.rowIndex][prevSelectedSeat.seatIndex].status = 'available';
        }
        
        // Mark the new seat as selected
        selectedSeat.status = 'selected';
        this.selectedSeat = { rowIndex, seatIndex };
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

.seat {
  width: 200px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  border: 1px solid #ccc;
  background-color: #d3d3d3; /* Default gray background */
  border-radius: 6px;
}

.occupied {
  background-color: red;
  color: white;
}

.available {
  background-color: rgb(205, 205, 205);
}

.selected {
  background-color: lightgreen;
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
</style>
