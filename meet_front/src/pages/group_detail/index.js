import React, {Component} from 'react'
import {List, Avatar, Icon, Card, Row, Col, Timeline} from 'antd'
import {withRouter} from 'react-router-dom'
import './index.less'
import axios from "../../network"
import Utils from "../../utils/utils"
import Api from "../../network/Api.config"


class GroupDetail extends Component {

    constructor(props) {
        super(props)
        this.state = {
            group_detail: {},
            group_success: false,
            task_id: this.props.match.params.task_id,
            summaries: [],
            file_success: false,
            files: []
        }
        this.params = {}
    }

    componentDidMount() {
        this.requestDetail()
        this.requestSummariesByTaskId()
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
            this.setState({
                group_success: true,
                group_detail: res
            })
            this.requestFiles(res.file_ids)
        })
    }

    requestSummariesByTaskId = () => {
        const {task_id} = this.state
        axios.ajax({
            url: '/public/summarys/tasks/' + task_id,
            method: 'get',
            data: {
                isShowLoading: true
            }
        }).then((res) => {
            this.setState({
                summary_success: true,
                summaries: res
            })
        })
    }

    requestFiles = (file_ids) => {
        axios.ajax({
            url: '/public/file/multi/',
            method: 'get',
            data: {
                params: {
                    file_ids: file_ids
                },
            }
        }).then((res) => {
            this.setState({
                file_success: true,
                files: res
            })
        })
    }

    formatSummary = (summary) => {
        return (
            <div className='summary_item'>
                <div className='users-container'>
                    {summary.user.title}
                </div>
                <div className='title-container'>
                    {summary.title}
                </div>
                <div className='desc-container'>
                    <div dangerouslySetInnerHTML={{__html: summary.description}}/>
                </div>
                <div className='time-container'>
                    <Icon type='clock-circle' style={{marginRight: 10}}/>{Utils.formateDate(summary.publish_time)}
                </div>
            </div>
        )
    }

    formatFile = (files) => {
        return files.map((item, index) => {
            return <div>
                <a href={item.url} key={index}>{item.name}</a>
            </div>
        })
    }

    render() {
        const {group_detail, group_success, summaries, file_success, files} = this.state
        return <div className='group_detail_page'>
            {group_success ? <Card
                className='card-wrap'
                cover={<div className='header-cover' alt='head_cover'
                            style={{backgroundImage: `url(${ group_detail.head_img}`}}/>}
            >
                <div className='avatar-container'>
                    {Utils.getIconAvatar(group_detail)}
                </div>
                <div className='time-container'>
                    <Icon type='clock-circle' style={{marginRight: 10}}/>{Utils.formateDate(group_detail.publish_time)}
                    <Icon type='team'
                          style={{marginRight: 10, marginLeft: 10}}/>{Utils.formateDate(group_detail.start_time)}
                </div>
                <div className='title-container'>
                    {group_detail.title}
                </div>
                <div className='desc-container'>
                    <div dangerouslySetInnerHTML={{__html: group_detail.description}}/>
                </div>
                <div className='users-container'>
                    汇报人: {group_detail.users.map((item) => item.username).join(',')}
                </div>
                <div className='file-container'>
                    {file_success && files.length > 0 ? <div>
                        <div>📎附件:</div>
                        {this.formatFile(files)}
                    </div> : ''}
                </div>
            </Card> : ''}
            <Card title='组会总结'>
                <List
                    itemLayout="horizontal"
                    dataSource={summaries}
                    renderItem={item => (
                        <List.Item>
                            <List.Item.Meta
                                avatar={<Avatar src={item.user.avatar}/>}
                                title={<a href="https://ant.design">{item.username}</a>}
                                description={this.formatSummary(item)}/>
                        </List.Item>
                    )}
                />
            </Card>
        </div>
    }

}

export default withRouter(GroupDetail)