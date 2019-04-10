import React, {Component} from 'react'
import {Form, Button, Checkbox, Input, Icon, Card, Row, Col} from 'antd'
import axios from '../../network'
import {withRouter} from 'react-router-dom'
import './index.less'
import PropTypes from 'prop-types'
import Background from '../../components/BackGround'
import Api from '../../network/Api.config'
import Utils from '../../utils/utils'

const FormItem = Form.Item

class Login extends Component {

    componentDidMount() {

    }

    handleLogin = (values) => {
        axios.ajax({
            url: Api.Urls.login,
            data: {
                params: values
            },
            method: 'post'
        }).then((res) => {
            const role = Utils.judgeRole(res.user.authorities)
            sessionStorage.setItem("role", role)
            sessionStorage.setItem("username", res.user.username)
            sessionStorage.setItem('avatar',res.user.avatar)
            this.props.history.push("/home")
        })
    }

    render() {
        return <div className='login_page'>
            <Background/>
            <Row>
                <Col
                    span={8} offset={8}>
                    <Card
                        className='login_container'
                        title='欢迎登录组会管理后台'>
                        <LoginForm handleLogin={(values) => this.handleLogin(values)}/>
                    </Card>
                </Col>
            </Row>
        </div>
    }

}

class LoginForm extends React.Component {
    handleSubmit = (e) => {
        e.preventDefault()
        this.props.form.validateFields((err, values) => {
            if (!err) {
                this.props.handleLogin(values)
            }
        })
    }


    render() {
        const formItemLayout = {
            labelCol: {span: 4},
            wrapperCol: {span: 19}
        }
        const offsetLayout = {
            wrapperCol: {
                offset: 3
            }
        }
        const {getFieldDecorator} = this.props.form
        return (
            <Form hideRequiredMark onSubmit={this.handleSubmit} className="login-form">
                <FormItem label='用户名' {...formItemLayout}>
                    {getFieldDecorator('username', {
                        rules: [{required: true, message: '用户名不为空!'}],
                    })(
                        <Input prefix={<Icon type="user" style={{color: 'rgba(0,0,0,.25)'}}/>} placeholder="请输入用户名"/>
                    )}
                </FormItem>
                <FormItem label='密码' {...formItemLayout}>
                    {getFieldDecorator('password', {
                        rules: [{required: true, message: '密码不为空!'}],
                    })(
                        <Input prefix={<Icon type="lock" style={{color: 'rgba(0,0,0,.25)'}}/>} type="password"
                               placeholder="请输入密码"/>
                    )}
                </FormItem>
                <FormItem {...offsetLayout}>
                    {getFieldDecorator('remember', {
                        valuePropName: 'checked',
                        initialValue: true,
                    })(
                        <Checkbox>记住我</Checkbox>
                    )}
                    <a className='login-form-register' href="">现在注册!</a>
                    <a className="login-form-forgot" href="">忘记密码</a>
                </FormItem>
                <FormItem className='login_button_wrapper'>
                    <Button type="primary" htmlType="submit" className="login-form-button">
                        登 录
                    </Button>
                </FormItem>
            </Form>
        )
    }
}

LoginForm.propTypes = {
    handleLogin: PropTypes.func.isRequired
}

LoginForm = Form.create({name: 'user_login'})(LoginForm)


export default withRouter(Login)