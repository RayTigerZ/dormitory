import { getRequest, postRequest, deleteRequest } from '../utils/request';
export function getRoles({ pageNum, pageSize }) {
	let data = { pageNum, pageSize };
	return getRequest('/roles', data);
}
export function deleteRole(rid) {
	return deleteRequest(`/roles/${rid}`);
}

export function saveRole(data) {
	return postRequest('/roles', data);
}

export function getUserPermission(rid) {
	return getRequest(`/roles/${rid}/permissions`);
}

export function setUserPermission(rid, ids) {
	return postRequest(`/roles/${rid}/permissions`, { ids });
}
