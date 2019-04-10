import axios from 'axios'
import {message} from 'antd'

axios.create({
    timeout: 15000,
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Access-Token': sessionStorage.getItem('access_token')
    }
})

axios.interceptors.request.use(
    config => {
        let authToken = sessionStorage.getItem('access_token')
        if (authToken) {
            config.headers['Access-Token'] = authToken
        }
        config.headers['Content-Type'] = 'application/x-www-form-urlencoded'
        return config
    },
    err => {
        message.error('加载超时')
        return Promise.reject(err)
    }
)

axios.interceptors.response.use(
    response => {
        const res = response.data
        if (res && res.success && res.data && res.data.token) {
            const token = res.data.token
            sessionStorage.setItem('access_token', token)
        }
        return res
    }, error => {
        return Promise.reject(error)
    }
)


export default axios