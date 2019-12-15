import { getMenu } from '@/api/permission';

export const isNotNullORBlank = (...args) => {
	for (var i = 0; i < args.length; i++) {
		var argument = args[i];
		if (argument == null || argument == '' || argument == undefined) {
			return false;
		}
	}
	return true;
};
export const initMenu = (router, store) => {
	if (store.state.menu != '') {
		return;
	}
	getMenu()
		.then(res => {
			//debugger;
			if (res && res.data.code == 200) {
				var menu = [res.data.data];
				var fmtRoutes = formatRoutes(menu);

				router.addRoutes(fmtRoutes);

				store.commit('setMenu', fmtRoutes[0]);
			}
		})
		.catch(err => {
			console.log(err);
		});
};
export const formatRoutes = routes => {
	let fmRoutes = [];
	routes.forEach(router => {
		let { path, component, name, keepAlive, requireAuth, iconClass, children, type } = router;
		if (children && children instanceof Array) {
			children = formatRoutes(children);
		}

		let fmRouter = {
			path: path,
			component: resolve => require(['../views/' + component + '.vue'], resolve),

			iconClass: iconClass,
			type: type,
			meta: {
				name,
				keepAlive,
				requireAuth,
				iconClass
			},
			children: children
		};

		fmRoutes.push(fmRouter);
	});
	return fmRoutes;
};

export const fileDownload = (data, fileName) => {
	let blob = new Blob([data], {
		type: 'application/octet-stream'
	});
	if (typeof window.navigator.msSaveBlob !== 'undefined') {
		window.navigator.msSaveBlob(blob, fileName);
	} else {
		let blobURL = window.URL.createObjectURL(blob);
		let tempLink = document.createElement('a');
		tempLink.style.display = 'none';
		tempLink.href = blobURL;
		tempLink.setAttribute('download', fileName);
		if (typeof tempLink.download === 'undefined') {
			tempLink.setAttribute('target', '_blank');
		}
		document.body.appendChild(tempLink);
		tempLink.click();
		document.body.removeChild(tempLink);
		window.URL.revokeObjectURL(blobURL);
	}
};

const getType = target => Object.prototype.toString.call(target);
export function deepClone(data) {
	var type = getType(data);
	var obj;
	if (type === 'array') {
		obj = [];
	} else if (type === 'object') {
		obj = {};
	} else {
		//不再具有下一层次
		return data;
	}
	if (type === 'array') {
		for (var i = 0, len = data.length; i < len; i++) {
			obj.push(deepClone(data[i]));
		}
	} else if (type === 'object') {
		for (var key in data) {
			obj[key] = deepClone(data[key]);
		}
	}
	return obj;
}
