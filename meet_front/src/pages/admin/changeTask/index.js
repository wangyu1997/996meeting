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

class AdminUpdateTask extends Component {
    constructor(props) {
        super(props)
        this.state = {
            formList: [],
            loadSuccess: false,
            formParams: {},
            task_id: this.props.match.params.task_id
        }
    }

    handleSubmit = (values) => {
        const description = values.description
        if (description instanceof EditorState)
            values.description = convertToRaw(values.description.getCurrentContent())
        const params = {
            title: values.title,
            description: draftToHtml(values.description),
            start_time: values.start_time.format('YYYY-MM-DD HH:mm'),
            user_ids: values.user_ids.join(','),
            head_img: values.head_img,
            file_ids: Utils.formatFileListAsId(values.file_ids)
        }
        this.updateTask(params)
    }

    requestDetail = () => {
        const {task_id} = this.state
        axios.ajax({
            url: Api.Urls.queryTaskById + task_id,
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
        const {task_id} = this.state
        axios.ajax({
            url: Api.Urls.adminUpdateTask + task_id,
            method: 'post',
            data: {
                params,
                isShowLoading: true
            },
        }).then((res) => {
                this.props.history.push("/admin/tasks")
                notification.success({
                    message: '通知',
                    description: '组会任务修改成功！'
                })
            }
        )
    }

    requestValues = (res) => {
        const title = res.title
        const description = Utils.backHtml2Edit(res.description)
        const start_time = new moment(res.start_time)
        const head_img = res.head_img
        const user_ids = res.users.map((user) => {
            return `${user.id}`
        })
        const file_ids = res.file_ids
        Utils.getFilesByIds(file_ids, (fileList) => {
                axios.ajax({
                    url: '/user/search/users',
                    data: {},
                    method: 'get'
                }).then((res) => {
                    this.setState({
                        loadSuccess: true,
                        formParams: {
                            title, description, start_time, user_ids, head_img, file_ids: {fileList: fileList},
                            searchData: [res]
                        }
                    })
                })
            }
        )
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
                    }), user_ids: Form.createFormField({
                        value: formParams.user_ids,
                    }), start_time: Form.createFormField({
                        value: formParams.start_time,
                    }), file_ids: Form.createFormField({
                        value: formParams.file_ids,
                    }), head_img: Form.createFormField({
                        value: formParams.head_img
                    })
                }
            },
        })(DetailForm)
        const {loadSuccess, formParams} = this.state
        return <div className='create_task_container'>
            <Card title='修改组会内容'>
                {loadSuccess ? <ChangeTaskForm
                    formParams={formParams}
                    formList={this.state.formList}
                    handleValues={(values) => this.handleSubmit(values)}/> : ''}
            </Card>
        </div>
    }
}

export default withRouter(AdminUpdateTask)