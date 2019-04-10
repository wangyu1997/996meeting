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

class UserCreateReport extends Component {
    constructor(props) {
        super(props)
        this.state = {
            formParams: []
        }
    }

    handleSubmit = (values) => {
        const params = {
            title: values.title,
            description: draftToHtml(values.description),
            start_time: values.start_time.format('YYYY-MM-DD HH:mm'),
            next_week_plan: values.next_week_plan,
            file_ids: Utils.formatFileListAsId(values.file_ids),
            isAdmin: false
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
                this.props.history.push("/user/reports")
                notification.success({
                    message: '通知',
                    description: '个人汇报发布成功！'
                })
            }
        )
    }

    componentDidMount() {
        this.requestValues()
    }

    requestValues = () => {
        this.setState({
            loadSuccess: true,
            formParams: {
                searchData: []
            }
        })
    }

    render() {
        const CreateTaskForm = Form.create()(DetailForm)
        const {formParams} = this.state
        return <div className='create_task_container'>
            <Card title='发布个人汇报'>
                <CreateTaskForm formList={Utils.addKeyToFormList(Scheme.formList)}
                                formParams={formParams}
                                handleValues={(values) => this.handleSubmit(values)}/>
            </Card>
        </div>
    }
}

export default withRouter(UserCreateReport)