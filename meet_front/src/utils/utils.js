import React from 'react'
import {Select, Avatar} from 'antd'
import htmlToDraft from "html-to-draftjs"
import {ContentState, EditorState} from "draft-js"
import axios from "../network"
import Api from "../network/Api.config"

const Option = Select.Option

export default {
    formateDate(time) {
        if (!time) return ''
        let date = new Date(time)
        let mon = (date.getMonth() + 1)
        let dd = date.getDate()
        let hh = date.getHours()
        let mm = date.getMinutes()
        if (mon < 10)
            mon = "0" + mon
        if (dd < 10)
            dd = "0" + dd
        if (hh < 10)
            hh = "0" + hh
        if (mm < 10)
            mm = "0" + mm
        return date.getFullYear() + '-' + mon + '-' + dd + ' ' + hh + ':' + mm
    },
    pagination(data, callback) {
        let page = {
            onChange: (current) => {
                callback(current)
            },
            current: parseInt(data.current),
            pageSize: data.page_size,
            total: data.total_size,
            showTotal: () => {
                return `共${data.total_size}条数据`
            },
            showQuickJumper: true
        }
        console.log(page)
        return page
    }, formatHour(time) {
        const hour = parseInt(time / 3600)
        time %= 3600
        const minute = parseInt(time / 60)
        time %= 60
        let str = ''
        if (hour > 0)
            str += hour + '小时'
        if (minute > 0)
            str += minute + '分钟'
        str += time + '秒'
        return str
    }, getOptionList(data) {
        if (!data)
            return []
        let option = []
        data.forEach((item, index) => {
            option.push(<Option value={item.id} key={item.id}>{item.name}</Option>)
        })
        return option
    }, getData(data) {
        let ret = ''
        for (let item in data)
            ret += encodeURIComponent(item) + '=' + encodeURIComponent(data[item]) + "&"
        return ret
    }, judgeRole(roles) {
        let isAdmin = false
        roles.forEach((item) => {
            if (item.authority.toLowerCase().endsWith('admin')) {
                isAdmin = true
            }
        })
        return isAdmin ? 'admin' : 'user'
    }, concatFilter(params, data) {
        for (let item in data)
            params[item] = data[item]
        return params
    }, formatTimeStamp(_this, format) {
        const date = {
            "M+": _this.getMonth() + 1,
            "d+": _this.getDate(),
            "h+": _this.getHours(),
            "m+": _this.getMinutes(),
            "s+": _this.getSeconds(),
            "q+": Math.floor((_this.getMonth() + 3) / 3),
            "S+": _this.getMilliseconds()
        }
        if (/(y+)/i.test(format))
            format = format.replace(RegExp.$1, (_this.getFullYear() + '').substr(4 - RegExp.$1.length))
        date.forEach((k) => {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length === 1 ?
                    date[k] : ("00" + date[k]).substr(("" + date[k]).length))
            }
        })
        return format
    }, formatFileListAsId(fileList) {
        if (!fileList || !fileList.fileList)
            return ''
        return fileList.fileList.map((item) => {
            return item.response.data.id
        }).join(',')
    }, deleteHtmlTag(str) {
        str = str.replace(/<[^>]+>|&[^>]+;/g, "").trim()//去掉所有的html标签和&nbsp;之类的特殊符合
        return str
    }, showText(text, numSub) {

        if (text == null) {
            return ""
        }
        if (text.length > numSub) {
            return text.substring(0, numSub - 1) + "..."
        }
        return text
    }, addKeyToFormList(formList) {
        return formList.map((item, index) => {
            item.key = index
            return item
        })
    }, backHtml2Edit(text) {
        const contentBlock = htmlToDraft(text)
        if (contentBlock) {
            const contentState = ContentState.createFromBlockArray(contentBlock.contentBlocks)
            return EditorState.createWithContent(contentState)
        }
    }, getFilesByIds: (file_ids, callback) => {
        axios.ajax({
            url: Api.Urls.getFilesByIds,
            data: {
                params: {
                    file_ids: file_ids
                }
            },
            method: 'get'
        }).then((res) => {
            const fileList = []
            res.forEach((item, index) => {
                fileList.push({
                    uid: `${index}`,
                    name: item.name,
                    status: 'done',
                    response: {success: true, msg: '', data: item},
                    url: item.url,
                })
            })
            callback(fileList)
        })
    }, getIconAvatar(data) {
        return data.users.map((item, index) => {
            return <Avatar key={index} style={{marginRight: 10}} src={item.avatar}/>
        })
    }, randomNum(minNum, maxNum) {
        switch (arguments.length) {
            case 1:
                return parseInt(Math.random() * minNum + 1, 10)
            case 2:
                return parseInt(Math.random() * (maxNum - minNum + 1) + minNum, 10)
            default:
                return 0
        }
    }
}