import React from 'react'
import {Select} from 'antd'

export default {
    formList: [
        {
            type: 'INPUT',
            label: '汇报主题',
            field: 'title',
            placeholder: '请输入个人汇报主题',
            rules: [{required: true, message: '个人汇报主题不为空'}],
            initialValue: '',
        }, {
            type: 'EDIT',
            label: '个人汇报内容',
            field: 'description',
            placeholder: '请输入个人汇报内容',
            rules: [{required: true, message: '个人汇报内容不为空'}],
            initialValue: '',
        }, {
            type: 'TEXT_AREA',
            label: '下阶段工作计划',
            field: 'next_week_plan',
            placeholder: '请输入下阶段工作计划',
            rules: [{required: true, message: '工作计划不为空'}],
            initialValue: '',
        }, {
            type: 'DATE_PICKER',
            label: '讨论时间',
            field: 'start_time',
            placeholder: '请选择讨论时间',
            rules: [{required: true, message: '请选择讨论时间'}],
        }, {
            type: 'UPLOAD',
            label: '上传附件',
            field: 'file_ids',
            rules: [],
        }
    ]
}