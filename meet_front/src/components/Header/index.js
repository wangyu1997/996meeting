import React, {Component} from 'react'
import {Col, Row, message} from "antd"
import './index.less'
import Util from '../../utils/utils'
import Axios from '../../network'
import {withRouter} from 'react-router-dom'

class Header extends Component {
    constructor(props) {
        super(props)
        this.state = {}
    }

    componentWillMount() {
        this.setState({
            username: sessionStorage.getItem("username")
        })
        setInterval(() => {
            const sysTime = Util.formateDate(new Date().getTime())
            this.setState({
                sysTime
            })
        }, 1000)
        this.getWeatherApi()
    }


    getWeatherApi() {
        const city = '310100'
        Axios.jsonp({
            url: 'https://restapi.amap.com/v3/weather/weatherInfo?key=437058cce8a92f4a100bd081b57cc20f&city=' + city
        }).then((res) => {
            this.setState({
                weather: res.lives[0].weather
            })
        }).then((err) => {
        })
    }

    handleLogout = () => {
        sessionStorage.clear()
        message.info("正在退出登录...")
        setTimeout(() => {
            this.props.history.push('/login')
        }, 500)


    }


    render() {
        const menuType = this.props.menuType
        return <div className='header'>
            <Row className='header-top'>{
                menuType ?
                    <Col span={6} className='logo'>
                        <img src='/assets/logo-ant.svg' alt=''/>
                        <span>组会管理系统</span>
                    </Col> : ''
            }
                <Col span={menuType ? 18 : 24}>
                    <span>欢迎，{this.state.username}</span>
                    <a onClick={this.handleLogout}>退出</a>
                </Col>
            </Row>{
            menuType ? '' :
                <Row className='breadcrumb'>
                    <Col span={4} className='breadcrumb-title'>
                        首页
                    </Col>
                    <Col span={20} className='weather'>
                        <span className='date'>{this.state.sysTime}</span>
                        <span className='weather-detail'>{this.state.weather}</span>
                    </Col>
                </Row>
        }
        </div>
    }

}

export default withRouter(Header)