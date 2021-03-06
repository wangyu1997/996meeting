import Utils from "../../../utils/utils"

export default {
    formList: [
        {
            type: 'INPUT',
            label: '主题',
            field: 'title',
            placeholder: '输入任务主题',
            initialValue: '',
        }, {
            type: 'INPUT',
            label: '描述',
            field: 'description',
            placeholder: '输入任务描述',
            initialValue: '',
        }, {
            type: 'TIME_QUERY',
            field: 'time_query',
            label: '开会时间'
        }, {
            type: 'TIME_QUERY',
            field: 'publish_time_query',
            label: '发布时间'
        }
    ],
    columns: [
        {
            title: '组会编号',
            dataIndex: 'id'
        }, {
            title: '主题',
            dataIndex: 'title'
        }, {
            title: '总结内容',
            dataIndex: 'description',
            render: (text) => {
                return Utils.showText(Utils.deleteHtmlTag(text), 30)
            }
        }, {
            title: '开始时间',
            dataIndex: 'start_time',
            render: (text) => {
                return Utils.formateDate(text)
            }
        }, {
            title: '发布时间',
            dataIndex: 'publish_time',
            render: (text) => {
                return Utils.formateDate(text)
            }
        }, {
            title: '讨论成员',
            dataIndex: 'users',
            render(text) {
                return text.map((item) => {
                    return item.username
                }).join(',')
            }
        }
    ]
}