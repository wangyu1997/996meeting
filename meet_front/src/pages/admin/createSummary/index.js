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

class AdminCreateSummary extends Component {
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
            user_id: values.user_id,
            task_id: values.task_id
        }
        this.addSummary(params)
    }

    addSummary = (params) => {
        axios.ajax({
            url: Api.Urls.adminAddSummary,
            method: 'post',
            data: {
                params,
                isShowLoading: true
            },
        }).then((res) => {
                this.props.history.push("/admin/summaries")
                notification.success({
                    message: '通知',
                    description: '组会小结发布成功！'
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
            const users = res
            axios.ajax({
                url: '/user/search/tasks',
                data: {},
                method: 'get'
            }).then((res) => {
                const tasks = res
                console.log(tasks)
                this.setState({
                    loadSuccess: true,
                    formParams: {
                        searchData: [users, tasks]
                    }
                })
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
            <Card title='发布组会小结'>
                {loadSuccess ?
                    <CreateTaskForm formList={Utils.addKeyToFormList(Scheme.formList)}
                                    formParams={formParams}
                                    handleValues={(values) => this.handleSubmit(values)}/> : ''}
            </Card>
        </div>
    }
}

export default withRouter(AdminCreateSummary)