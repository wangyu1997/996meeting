import React from 'react'
import {Select} from 'antd'

export default {
    formList: [
        {
            type: 'INPUT',
            label: '小结主题',
            field: 'title',
            placeholder: '请输入组会小结主题',
            rules: [{required: true, message: '组会小结主题不为空'}],
            initialValue: '',
        }, {
            type: 'EDIT',
            label: '小结内容',
            field: 'description',
            placeholder: '请输入组会内容',
            rules: [{required: true, message: '组会小结内容不为空'}],
            initialValue: '',
        },{
            type: 'SELECT',
            index: 0,
            label: '组会任务',
            mode: 'single',
            field: 'task_id',
            placeholder: '请选择提交的组会任务',
            rules: [{required: true, message: '请选择提交的组会任务'}],
        }
    ]
}