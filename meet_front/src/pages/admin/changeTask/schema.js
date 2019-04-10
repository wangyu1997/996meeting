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
            type: 'DATE_PICKER',
            label: '开会时间',
            field: 'start_time',
            placeholder: '请选择开会时间',
            rules: [{required: true, message: '请选择开会时间'}],
        }, {
            type: 'SELECT',
            index: 0,
            mode: 'multiple',
            label: '参会成员',
            field: 'user_ids',
            initialValue: [],
            placeholder: '请选择参会成员',
            rules: [{required: true, message: '请至少选择一个参与成员'}],
        }, {
            type: 'INPUT',
            label: '头部图像',
            field: 'head_img',
            placeholder: '请输入头图',
            rules: [{required: true, message: '头图不为空'}],
            initialValue: '',
        }, {
            type: 'UPLOAD',
            label: '上传附件',
            field: 'file_ids',
            rules: [],
        }
    ]
}