import Utils from "../../../utils/utils"
import React from 'react'
import {Avatar, Badge} from 'antd'

export default {
    formList: [
        {
            type: 'INPUT',
            label: '用户名',
            field: 'username',
            placeholder: '输入用户名',
            initialValue: '',
        }, {
            type: 'INPUT',
            label: '邮箱',
            field: 'email',
            placeholder: '输入邮箱',
            initialValue: '',
        }, {
            type: 'INPUT',
            label: '联系方式',
            field: 'contact',
            placeholder: '输入联系方式',
            initialValue: '',
        }
    ],
    columns: [
        {
            title: '用户编号',
            dataIndex: 'id'
        }, {
            title: '头像',
            dataIndex: 'avatar',
            render: (src) => {
                console.log(src)
                return <Avatar src={src}/>
            }
        }, {
            title: '用户名',
            dataIndex: 'username'
        }, {
            title: '邮箱',
            dataIndex: 'email'
        }, {
            title: '联系方式',
            dataIndex: 'contact'
        }, {
            title: '用户权限',
            dataIndex: 'authorities',
            render: (authorities) => {
                if (Utils.judgeRole(authorities) === 'admin')
                    return <Badge status={"success"} text={'管理员'}/>
                return <Badge status={"default"} text={'用户'}/>
            }
        }
    ]
}