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
            type: 'INPUT',
            label: '下阶段工作计划',
            field: 'next_week_plan',
            placeholder: '输入下阶段工作计划',
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
            title: '汇报编号',
            dataIndex: 'id'
        }, {
            title: '主题',
            dataIndex: 'title'
        }, {
            title: '内容',
            dataIndex: 'description',
            render: (text) => {
                return Utils.showText(Utils.deleteHtmlTag(text), 30)
            }
        }, {
            title: '下阶段任务',
            dataIndex: 'next_week_plan'
        }, {
            title: '讨论时间',
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
            title: '发布人',
            dataIndex: 'user',
            render(text) {
                return text.username
            }
        }
    ]
}