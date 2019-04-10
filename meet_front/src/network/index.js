import JsonP from 'jsonp'
import axios from './server'
import {Modal} from 'antd'
import Utils from "../utils/utils"
import Api from './Api.config'


export default class Axios {
    static jsonp(options) {
        return new Promise((resolve, reject) => {
            JsonP(options.url, {
                param: 'callback'
            }, (err, response) => {
                if (response.status === '0')
                    reject(response.info)
                resolve(response)
            })
        })
    }

    static ajax(options) {
        let loading
        if (options.data && options.data.isShowLoading) {
            loading = document.getElementById('ajaxLoading')
            loading.style.display = 'block'
        }
        const baseApi = Api.BaseUrl
        const query = {
            url: options.url,
            method: options.method,
            baseURL: baseApi,
            headers: {...options.header},
            data: (options.method === 'post' && options.data && options.data.params) || '',
            params: (options.method === 'get' && options.data && options.data.params) || ''
        }
        if (options.method === 'post' && options.data && options.data.params&&!options.data.isUpload)
            query.data = Utils.getData(options.data.params)
        if (options.method === 'get' && options.data && options.data.params)
            query.params = options.data.params
        return new Promise((resolve, reject) => {
            axios(query).then((response) => {
                if (options.data && options.data.isShowLoading) {
                    loading = document.getElementById('ajaxLoading')
                    loading.style.display = 'none'
                }
                if (response) {
                    if (response.success) {
                        resolve(response.data)
                    } else {
                        if (response.code === 403)
                            response.msg = "登录失效或权限不足"
                        Modal.info({
                            title: '提示',
                            content: response.msg
                        })
                    }
                }
                else {
                    if (options.data && options.data.isShowLoading) {
                        loading = document.getElementById('ajaxLoading')
                        loading.style.display = 'none'
                    }
                    reject(response.data)
                }
            })
        })
    }

    static requestList(_this, url, params) {
        const _func = this
        const data = {
            params: params,
            isShowLoading: true
        }
        this.ajax({
            url: url,
            data,
            method: 'get'
        }).then((res) => {
            if (res && res.list) {
                let list = res.list.map((item, index) => {
                    item.key = index
                    return item
                })
                console.log(_this.params)
                console.log(list)
                _this.setState({
                    list: list,
                    pagination: Utils.pagination(res, (current) => {
                        _this.params.page = current
                        _func.requestList(_this, url, _this.params)
                    })
                })
            }
        })
    }
}