import axios from 'axios';
import { Message } from 'element-ui';
import store from '../store';
import { Loading } from 'element-ui';
import context from '@/main.js';
import qs from 'qs';

// loading框设置局部刷新，且所有请求完成后关闭loading框
let loadingInstance;
//声明一个对象用于存储请求个数
let needLoadingRequestCount = 0;

function startLoading() {
	// console.log('loading start')
	loadingInstance = Loading.service({
		lock: true,
		text: '努力加载中',
		target: document.querySelector('.loading-area') //设置加载动画区域
	});
}

function endLoading() {
	// console.log('loading end');
	context.$nextTick(() => {
		// 以服务的方式调用的 Loading 需要异步关闭
		loadingInstance.close();
	});
}

function showFullScreenLoading() {
	if (needLoadingRequestCount === 0) {
		startLoading();
	}
	needLoadingRequestCount++;
}

function tryHideFullScreenLoading() {
	if (needLoadingRequestCount <= 0) return;
	needLoadingRequestCount--;
	if (needLoadingRequestCount === 0) {
		endLoading();
	}
}

const stringify = function(data) {
	return qs.stringify(data, {
		indices: false,
		arrayFormat: 'repeat'
	});
};

//Axios全局实例
const instance = axios.create({
	baseURL: 'http://localhost:8090',
	transformRequest: [
		function(data) {
			// Do whatever you want to transform the data
			return stringify(data);
		}
	],
	paramsSerializer: function(params) {
		return stringify(params);
	}
});

instance.interceptors.request.use(
	config => {
		showFullScreenLoading();
		config.headers.Authorization = store.state.token;
		return config;
	},
	err => {
		tryHideFullScreenLoading();

		Message.error('请求超时!');
		// return Promise.resolve(err);
		return err;
	}
);

instance.interceptors.response.use(
	res => {
		tryHideFullScreenLoading();
		if (res.headers && res.headers['fileName']) {
			return res;
		}
		if (res.status && res.status == 200 && res.data.code && res.data.code != 200) {
			Message.error(res.data.msg);
		}
		// if (data.data.msg) {
		//   Message.success(data.data.msg);
		// }
		return res;
	},
	err => {
		tryHideFullScreenLoading();
		// debugger;

		if (err.response) {
			if (err.response.status == 504 || err.response.status == 404) {
				Message.error('服务器被吃了!⊙﹏⊙');
			} else if (err.response.status == 403) {
				Message.error('权限不足,请联系管理员!');
			} else if (err.response.status == 401) {
				Message.error(err.response.data.msg);
			} else {
				if (err.response.data.msg) {
					Message.error(err.response.data.msg);
				}
			}
		} else {
			Message.error(err.message);
		}
		return err;
	}
);

export const postRequest = (url, data) => {
	return instance.post(url, data, {
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded'
		}
	});
};
export const uploadFileRequest = (url, data) => {
	return instance.post(url, data, {
		headers: {
			'Content-Type': 'multipart/form-data'
		},
		transformRequest: [
			function(data) {
				// 上传文件时不对data进行处理（重点）
				return data;
			}
		]
	});
};

export const putRequest = (url, data) => {
	return instance.put(url, data, {
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded'
		}
	});
};

export const deleteRequest = (url, params) => {
	return instance.delete(url, {
		params
	});
};

export const getRequest = (url, params) => {
	return instance.get(url, {
		params
	});
};

// 文件下载
export const downloadRequest = (url, params) => {
	return instance.get(url, {
		params: params,
		responseType: 'arraybuffer'
	});
};

export const exportExcel = (url, options = {}) => {
	return new Promise((resolve, reject) => {
		console.log(`${url} 请求数据，参数=>`, JSON.stringify(options));
		axios.defaults.headers['content-type'] = 'application/json;charset=UTF-8';
		axios({
			method: 'post',
			url: url, // 请求地址
			data: options, // 参数
			responseType: 'blob' // 表明返回服务器返回的数据类型
		}).then(
			response => {
				resolve(response.data);
				let blob = new Blob([response.data], {
					type: 'application/vnd.ms-excel'
				});
				console.log(blob);
				let fileName = Date.parse(new Date()) + '.xlsx';
				if (window.navigator.msSaveOrOpenBlob) {
					// console.log(2)
					navigator.msSaveBlob(blob, fileName);
				} else {
					// console.log(3)
					var link = document.createElement('a');
					link.href = window.URL.createObjectURL(blob);
					link.download = fileName;
					link.click();
					//释放内存
					window.URL.revokeObjectURL(link.href);
				}
			},
			err => {
				reject(err);
			}
		);
	});
};
