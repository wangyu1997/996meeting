import React, {Component} from 'react'
import {Form, Button, Checkbox, Input, Icon, Card, Row, Col} from 'antd'
import {withRouter} from 'react-router-dom'
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


    getUrl = () => {
        const num = Utils.randomNum(1, 25)
        console.log(`/gallery/${num}.png`)
        return `/gallery/${num}.png`
    }

    formatCard = (list) => {
        return list.map((item, index) => {
            const url = this.getUrl()
            return <Card
                key={index}
                className='card-wrap'
                cover={<div className='header-cover' alt='head_cover'
                            style={{backgroundImage: `url(${url}`}}/>}
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
            </Card>
        })
    }

    render() {
        this.getUrl()
        const {list} = this.state
        console.log(list)
        return <div className='group_page'>
            {this.formatCard(list)}
        </div>
    }

}

export default withRouter(Group)