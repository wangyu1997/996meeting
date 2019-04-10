import React from 'react'
import {Select} from 'antd'

export default {
    formList: [
        {
            type: 'INPUT',
            label: '组会主题',
            field: 'title',
            placeholder: '请输入组会主题',
            rules: [{required: true, message: '组会主题不为空'}],
            initialValue: '',
        }, {
            type: 'EDIT',
            label: '组会内容',
            field: 'description',
            placeholder: '请输入组会内容',
            rules: [{required: true, message: '组会内容不为空'}],
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
            label: '开会时间',
            field: 'start_time',
            placeholder: '请选择开会时间',
            rules: [{required: true, message: '请选择开会时间'}],
        }, {
            type: 'SELECT',
            index: 0,
            mode: 'single',
            label: '发布人',
            field: 'user_id',
            initialValue: [],
            placeholder: '请选择发布人',
            rules: [{required: true, message: '请至少选择一个发布人'}],
        }, {
            type: 'UPLOAD',
            label: '上传附件',
            field: 'file_ids',
            rules: [],
        }
    ]
}