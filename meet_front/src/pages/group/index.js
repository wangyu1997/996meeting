import React, {Component} from 'react'
import {Form, Button, Checkbox, Input, Icon, Card, Row, Col} from 'antd'
import {withRouter, Link} from 'react-router-dom'
import './index.less'
import axios from "../../network"
import Utils from "../../utils/utils"


class Group extends Component {

    constructor(props) {
        super(props)
        this.state = {
            list: []
        }
        this.params = {}
    }

    componentDidMount() {
        this.requestList()
    }

    requestList = () => {
        const data = {
            isShowLoading: true
        }
        axios.ajax({
            url: '/public/tasks',
            data,
            method: 'get'
        }).then((res) => {
            if (res) {
                this.setState({
                    list: res,
                })
            }
        })
    }


    formatCard = (list) => {
        return list.map((item, index) => {
            return <Card
                onClick={() => {
                    this.props.history.push(`/user/task/detail/${item.id}`)
                }}
                key={index}
                className='card-wrap'
                cover={<div className='header-cover' alt='head_cover'
                            style={{backgroundImage: `url(${item.head_img}`}}/>}
            >
                <div className='title-container'>
                    {item.title}
                </div>
                <div className='desc-container'>
                    {Utils.showText(Utils.deleteHtmlTag(item.description), 500)}
                </div>
                <div className='avatar-container'>
                    {Utils.getIconAvatar(item)}
                </div>
                <div className='time-container'>
                    <Icon type='clock-circle' style={{marginRight: 10}}/>{Utils.formateDate(item.publish_time)}
                    <Icon type='team' style={{marginRight: 10, marginLeft: 10}}/>{Utils.formateDate(item.start_time)}
                </div>
            </Card>
        })
    }

    render() {
        const {list} = this.state
        return <div className='group_page home-wrap'>
            {this.formatCard(list)}
        </div>
    }

}

export default withRouter(Group)