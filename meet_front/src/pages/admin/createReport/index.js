import React, {Component} from 'react'
import {Card, Form, notification} from 'antd'
import '../../../components/DetailForm/index.less'
import DetailForm from '../../../components/DetailForm'
import Scheme from './schema'
import draftToHtml from 'draftjs-to-html'
import Utils from '../../../utils/utils'
import axios from '../../../network'
import Api from '../../../network/Api.config'
import {withRouter} from "react-router-dom"

class AdminCreateReport extends Component {
    constructor(props) {
        super(props)
        this.state = {
            loadSuccess: false,
            formParams: []
        }
    }

    handleSubmit = (values) => {
        const params = {
            title: values.title,
            description: draftToHtml(values.description),
            start_time: values.start_time.format('YYYY-MM-DD HH:mm'),
            user_id: values.user_id,
            next_week_plan: values.next_week_plan,
            file_ids: Utils.formatFileListAsId(values.file_ids)
        }
        this.addReport(params)
    }

    addReport = (params) => {
        console.log(params)
        axios.ajax({
            url: Api.Urls.adminAddReport,
            method: 'post',
            data: {
                params,
                isShowLoading: true
            },
        }).then((res) => {
                this.props.history.push("/admin/reports")
                notification.success({
                    message: '通知',
                    description: '个人汇报发布成功！'
                })
            }
        )
    }

    requestValues = () => {
        axios.ajax({
            url: '/user/search/users',
            data: {},
            method: 'get'
        }).then((res) => {
            this.setState({
                loadSuccess: true,
                formParams: {
                    searchData: [res]
                }
            })
        })
    }

    componentDidMount() {
        this.requestValues()
    }

    render() {
        const CreateTaskForm = Form.create()(DetailForm)
        const {loadSuccess, formParams} = this.state
        return <div className='create_task_container'>
            <Card title='发布个人汇报'>
                {loadSuccess ?
                    <CreateTaskForm formList={Utils.addKeyToFormList(Scheme.formList)}
                                    formParams={formParams}
                                    handleValues={(values) => this.handleSubmit(values)}/> : ''}
            </Card>
        </div>
    }
}

export default withRouter(AdminCreateReport)