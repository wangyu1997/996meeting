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
import {EditorState, convertToRaw} from 'draft-js'

class AdminUpdateSummary extends Component {
    constructor(props) {
        super(props)
        this.state = {
            formList: [],
            loadSuccess: false,
            formParams: {},
            summary_id: this.props.match.params.summary_id
        }
    }

    handleSubmit = (values) => {
        const description = values.description
        if (description instanceof EditorState)
            values.description = convertToRaw(values.description.getCurrentContent())
        const params = {
            title: values.title,
            description: draftToHtml(values.description),
            task_id: values.task_id,
            user_id: values.user_id
        }
        this.updateTask(params)
    }

    requestDetail = () => {
        const {summary_id} = this.state
        axios.ajax({
            url: Api.Urls.querySummaryById + summary_id,
            method: 'get',
            data: {
                isShowLoading: true
            }
        }).then((res) => {
            const formList = Utils.addKeyToFormList(Scheme.formList)
            this.requestValues(res)
            this.setState({formList})
        })
    }

    componentDidMount() {
        this.requestDetail()
    }

    updateTask = (params) => {
        const {summary_id} = this.state
        axios.ajax({
            url: Api.Urls.adminUpdateSummary + summary_id,
            method: 'post',
            data: {
                params,
                isShowLoading: true
            },
        }).then((res) => {
                this.props.history.push("/admin/summaries")
                notification.success({
                    message: '通知',
                    description: '组会小结修改成功！'
                })
            }
        )
    }

    requestValues = (res) => {
        const title = res.title
        const description = Utils.backHtml2Edit(res.description)
        const user_id = `${res.user.id}`
        const task_id = `${res.task_id}`
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
                this.setState({
                    loadSuccess: true,
                    formParams: {
                        title, description, task_id, user_id,
                        searchData: [users, tasks]
                    }
                })
            })
        })
    }


    render() {
        const ChangeTaskForm = Form.create({
            mapPropsToFields(props) {
                const {formParams} = props
                return {
                    title: Form.createFormField({
                        value: formParams.title,
                    }),
                    description: Form.createFormField({
                        value: formParams.description
                    }), user_id: Form.createFormField({
                        value: formParams.user_id,
                    }), task_id: Form.createFormField({
                        value: formParams.task_id,
                    })
                }
            },
        })(DetailForm)
        const {loadSuccess, formParams} = this.state
        return <div className='create_task_container'>
            <Card title='修改组会小结内容'>
                {loadSuccess ? <ChangeTaskForm
                    formParams={formParams}
                    formList={this.state.formList}
                    handleValues={(values) => this.handleSubmit(values)}/> : ''}
            </Card>
        </div>
    }
}

export default withRouter(AdminUpdateSummary)