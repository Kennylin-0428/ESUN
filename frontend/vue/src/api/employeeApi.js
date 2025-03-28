import api from './axios';


export const getAllEmployee = () => {
    return api.get(`/employee/all`);
};

export const getEmployeeById = (empId) => {
    return api.get(`/employee/seating/${empId}`);
};

export const createEmployee = (employeeData) => {
    return api.post('/employee/add', employeeData);
};

export const deleteEmployee = (empId) => {
    return api.delete(`/employee/delete/${empId}`);
};