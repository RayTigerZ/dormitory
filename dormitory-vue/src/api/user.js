import { getRequest, postRequest, uploadFileRequest } from '@/utils/request';

export function batchSave(data) {
	return uploadFileRequest('/user/batchSave', data);
}

export function getPageOfUser({
	pageNum = 1,
	pageSize = 10,
	account = '',
	roleId = '',
	classId = []
}) {
	let data = {
		pageNum,
		pageSize,
		account,
		roleId,
		classId
	};
	return getRequest('/user/list', data);
}

export function saveUser(user) {
	return postRequest('/user/save', user);
}

export function enable(uid) {
	return postRequest('/user/enable', { uid });
}
