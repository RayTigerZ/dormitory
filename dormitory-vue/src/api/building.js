import { getRequest, postRequest } from '@/utils/request';

export function getBuildings({ pageNum, pageSize }) {
	let data = {
		pageNum,
		pageSize
	};
	return getRequest('/building/list', data);
}

export function saveBuilding(data) {
	return postRequest('/building/save', data);
}

export function getFloors(buildingId) {
	return getRequest('/building/floors', {
		buildingId
	});
}
