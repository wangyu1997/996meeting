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
import moment from 'moment'
import {EditorState, convertToRaw} from 'draft-js'

class UserUpdateReport extends Component {
    constructor(props) {
        super(props)
        this.state = {
            formList: [],
            loadSuccess: false,
            formParams: {},
            report_id: this.props.match.params.report_id
        }
    }

    handleSubmit = (values) => {
        const description = values.description
        if (description instanceof EditorState)
            values.description = convertToRaw(values.description.getCurrentContent())
        const params = {
            title: values.title,
            description: draftToHtml(values.description),
            next_week_plan: values.next_week_plan,
            start_time: values.start_time.format('YYYY-MM-DD HH:mm'),
            user_id: this.state.formParams.user_id,
            file_ids: Utils.formatFileListAsId(values.file_ids),
        }
        this.updateTask(params)
    }

    requestDetail = () => {
        const {report_id} = this.state
        axios.ajax({
            url: Api.Urls.queryReportById + report_id,
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
        const {report_id} = this.state
        axios.ajax({
            url: Api.Urls.adminUpdateReport + report_id,
            method: 'post',
            data: {
                params,
                isShowLoading: true
            },
        }).then((res) => {
                this.props.history.push("/user/reports")
                notification.success({
                    message: '通知',
                    description: '个人汇报修改成功！'
                })
            }
        )
    }

    requestValues = (res) => {
        const title = res.title
        const description = Utils.backHtml2Edit(res.description)
        const start_time = new moment(res.start_time)
        const user_id = `${res.user.id}`
        const next_week_plan = res.next_week_plan
        const file_ids = res.file_ids
        Utils.getFilesByIds(file_ids, (fileList) => {
            this.setState({
                loadSuccess: true,
                formParams: {
                    title, description, start_time, next_week_plan, user_id, file_ids: {fileList: fileList},
                    searchData: []
                }
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
                    }), next_week_plan: Form.createFormField({
                        value: formParams.next_week_plan
                    }), start_time: Form.createFormField({
                        value: formParams.start_time,
                    }), file_ids: Form.createFormField({
                        value: formParams.file_ids,
                    })
                }
            },
        })(DetailForm)
        const {loadSuccess, formParams} = this.state
        return <div className='create_task_container'>
            <Card title='修改个人汇报内容'>
                {loadSuccess ? <ChangeTaskForm
                    formParams={formParams}
                    formList={this.state.formList}
                    handleValues={(values) => this.handleSubmit(values)}/> : ''}
            </Card>
        </div>
    }
}

export default withRouter(UserUpdateReport)