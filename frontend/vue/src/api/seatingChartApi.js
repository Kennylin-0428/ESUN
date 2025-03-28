import axios from './axios';


export const getAllSeatingChart = () => {
  return axios.get('/seatingchart/all');
};

export const getSeatingChartByFloor = (floorNo) => {
  return axios.get(`/seatingchart/floor/${floorNo}`);
};

export const getSeatingChartStatus = (floorNo, seatNo) => {
  return axios.get(`/seatingchart/status`, {
    params: {
      floorNo: floorNo,
      seatNo: seatNo,
    },
  });
};

export const assignSeatToEmployee = (assignmentData) => {
  return axios.post('/seatingchart/assign', assignmentData);
};

export const removeEmployeeFromSeat = (clearData) => {
  return axios.put('/seatingchart/clear', clearData);
};

export const getAvailableSeats = () => {
  return axios.get('/seatingchart/available');
};
