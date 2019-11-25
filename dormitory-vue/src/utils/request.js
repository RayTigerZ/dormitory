import axios from 'axios'
import { Message } from 'element-ui'
import store from '../store'
import { Loading } from 'element-ui'
import context from '@/main.js'
// loading框设置局部刷新，且所有请求完成后关闭loading框
let loadingInstance

function startLoading() {
    console.log('loading start')
    loadingInstance = Loading.service({
        lock: true,
        text: 'Loading...',
        target: document.querySelector('.loading-area') //设置加载动画区域
    })
}

function endLoading() {
    console.log('loading end')
    context.$nextTick(() => {
        // 以服务的方式调用的 Loading 需要异步关闭
        loadingInstance.close()
    })
}

//声明一个对象用于存储请求个数
let needLoadingRequestCount = 0

function showFullScreenLoading() {
    if (needLoadingRequestCount === 0) {
        startLoading()
    }
    needLoadingRequestCount++
}

function tryHideFullScreenLoading() {
    if (needLoadingRequestCount <= 0) return
    needLoadingRequestCount--
    if (needLoadingRequestCount === 0) {
        endLoading()
    }
}
axios.interceptors.request.use(
    config => {
        showFullScreenLoading()
        config.headers.Authorization = store.state.token
        return config
    },
    err => {
        tryHideFullScreenLoading()
        console.log(err)
        Message.error({
                message: '请求超时!'
            })
            // return Promise.resolve(err);
    }
)

axios.interceptors.response.use(
    data => {
        tryHideFullScreenLoading()
        if (data.status && data.status == 200 && data.data.code == 302) {
            Message.error(data.data.msg)
            return
        }
        // if (data.data.msg) {
        //   Message.success(data.data.msg);
        // }
        return data
    },
    err => {
        tryHideFullScreenLoading()
        console.log(err)
        if (err.response.status == 504 || err.response.status == 404) {
            Message.error('服务器被吃了⊙﹏⊙∥')
        } else if (err.response.status == 403) {
            Message.error('权限不足,请联系管理员!')
        } else if (err.response.status == 401) {
            Message.error(err.response.data.msg)
        } else {
            if (err.response.data.msg) {
                Message.error(err.response.data.msg)
            } else {
                Message.error('未知错误!')
            }
        }
        // return Promise.resolve(err);
    }
)
let base = 'http://localhost:8090'
export const postRequest = (url, params, responseType) => {
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: params,
        transformRequest: [
            function(data) {
                let ret = ''
                for (let it in data) {
                    ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                }
                return ret
            }
        ],
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        responseType: responseType
    })
}
export const uploadFileRequest = (url, params) => {
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: params,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

export const putRequest = (url, params) => {
    return axios({
        method: 'put',
        url: `${base}${url}`,
        data: params,
        transformRequest: [
            function(data) {
                let ret = ''
                for (let it in data) {
                    ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                }
                return ret
            }
        ],
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
}

export const deleteRequest = url => {
    return axios({
        method: 'delete',
        url: `${base}${url}`
    })
}

export const getRequest = (url, params) => {
    return axios({
        method: 'get',
        url: `${base}${url}`,
        params: params
    })
}

export const exportExcel = (url, options = {}) => {
    return new Promise((resolve, reject) => {
        console.log(`${url} 请求数据，参数=>`, JSON.stringify(options))
        axios.defaults.headers['content-type'] = 'application/json;charset=UTF-8'
        axios({
            method: 'post',
            url: url, // 请求地址
            data: options, // 参数
            responseType: 'blob' // 表明返回服务器返回的数据类型
        }).then(
            response => {
                resolve(response.data)
                let blob = new Blob([response.data], {
                    type: 'application/vnd.ms-excel'
                })
                console.log(blob)
                let fileName = Date.parse(new Date()) + '.xlsx'
                if (window.navigator.msSaveOrOpenBlob) {
                    // console.log(2)
                    navigator.msSaveBlob(blob, fileName)
                } else {
                    // console.log(3)
                    var link = document.createElement('a')
                    link.href = window.URL.createObjectURL(blob)
                    link.download = fileName
                    link.click()
                        //释放内存
                    window.URL.revokeObjectURL(link.href)
                }
            },
            err => {
                reject(err)
            }
        )
    })
}