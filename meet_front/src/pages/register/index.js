import React, {Component} from 'react'
import {Form, Button, Upload, Input, Select, Icon, Card, Row, Col, message, Avatar} from 'antd'
import axios from '../../network'
import {withRouter} from 'react-router-dom'
import './index.less'
import PropTypes from 'prop-types'
import Background from '../../components/BackGround'
import Api from '../../network/Api.config'

const FormItem = Form.Item
const Option = Select.Option

class Register extends Component {

    componentDidMount() {

    }

    handleRegister = (values) => {
        if (values.avatar && values.avatar.file && values.avatar.data)
            values.avatar = values.avatar.file.response.data.url
        values .avatar = ''
        axios.ajax({
            url: Api.Urls.register,
            data: {
                params: values
            },
            method: 'post'
        }).then((res) => {
            this.props.history.replace('/login')
            message.success("注册成功！")
        })
    }

    render() {
        return <div className='register_page'>
            <Background/>
            <Row>
                <Col
                    span={8} offset={8}>
                    <Card
                        className='register_container'
                        title='欢迎注册组会管理后台'>
                        <RegisterForm
                            history={this.props.history}
                            handleRegister={(values) => this.handleRegister(values)}/>
                    </Card>
                </Col>
            </Row>
        </div>
    }

}

class RegisterForm extends React.Component {

    state = {
        confirmDirty: false,
        autoCompleteResult: [],
        history: this.props.history,
        loading: false,
    }

    handleSubmit = (e) => {
        e.preventDefault()
        this.props.form.validateFields((err, values) => {
            if (!err) {
                this.props.handleRegister(values)
            }
        })
    }

    beforeUpload(file) {
        const isJPG = ['image/gif', 'image/jpeg', 'image/jpg', 'image/png', 'image/svg'].indexOf(file.type)
        if (!isJPG) {
            message.error('只能上传jpg或者png的图片!')
        }
        const isLt2M = file.size / 1024 / 1024 < 2
        if (!isLt2M) {
            message.error('图片大小不能超过2MB!')
        }
        return isJPG && isLt2M
    }


    handleConfirmBlur = (e) => {
        const value = e.target.value
        this.setState({confirmDirty: this.state.confirmDirty || !!value})
    }

    compareToFirstPassword = (rule, value, callback) => {
        const form = this.props.form
        if (value && value !== form.getFieldValue('password')) {
            callback('密码输入不匹配!')
        } else {
            callback()
        }
    }

    validateToNextPassword = (rule, value, callback) => {
        const form = this.props.form
        if (value && this.state.confirmDirty) {
            form.validateFields(['confirm'], {force: true})
        }
        callback()
    }


    render() {
        const {getFieldDecorator} = this.props.form
        const prefixSelector = getFieldDecorator('prefix', {
            initialValue: '86',
        })(
            <Select style={{width: 70}}>
                <Option value="86">+86</Option>
                <Option value="87">+87</Option>
            </Select>
        )

        const formItemLayout = {
            labelCol: {span: 4},
            wrapperCol: {span: 19}
        }
        const offsetLayout = {
            labelCol: {span: 4},
            wrapperCol: {
                offset: 4,
                span: 9
            }
        }
        const uploadButton = (
            <div>
                <Icon type={this.state.loading ? 'loading' : 'plus'}/>
                <div className="ant-upload-text">Upload</div>
            </div>
        )
        const props = {
            action: `${Api.BaseUrl}${Api.Urls.uploadAvatar}`,
            headers: {
                'Access-Token': sessionStorage.getItem('access_token')
            },
            accept: 'image/gif,image/jpeg,image/jpg,image/png,image/svg',
            onChange: ({file, fileList}) => {
                if (file.status === 'uploading') {
                    this.setState({loading: true})
                    return
                }
                if (file.status === 'done') {
                    this.setState({
                        imageUrl: file.response.data.url,
                        loading: false
                    })
                }
            },
        }
        const imageUrl = this.state.imageUrl
        return (
            <Form hideRequiredMark onSubmit={this.handleSubmit} className="register-form">
                <FormItem label='头像' {...offsetLayout}>
                    {getFieldDecorator('avatar', {})(
                        <Upload
                            // style={{width:100,height:100}}
                            name="file"
                            listType="picture-card"
                            className="avatar-uploader"
                            showUploadList={false}
                            {...props}
                            beforeUpload={this.beforeUpload}
                        >
                            {imageUrl ?
                                <img className='avatar-img' style={{}} src={imageUrl} alt="avatar"/> : uploadButton}
                        </Upload>)}
                </FormItem>
                <FormItem label='用户名' {...formItemLayout}>
                    {getFieldDecorator('username', {
                        rules: [{required: true, message: '用户名不为空!'}],
                    })(
                        <Input prefix={<Icon type="user" style={{color: 'rgba(0,0,0,.25)'}}/>} placeholder="请输入用户名"/>
                    )}
                </FormItem>
                <Form.Item
                    {...formItemLayout}
                    label="密码"
                >
                    {getFieldDecorator('password', {
                        rules: [{
                            required: true, message: '请输入密码!',
                        }, {
                            validator: this.validateToNextPassword,
                        }, {
                            min: 6, message: '密码最少为6位'
                        }],
                    })(
                        <Input prefix={<Icon type="lock" style={{color: 'rgba(0,0,0,.25)'}}/>} placeholder="请输入密码"
                               type="password"/>
                    )}
                </Form.Item>
                <Form.Item
                    {...formItemLayout}
                    label="密码确认"
                >
                    {getFieldDecorator('confirm', {
                        rules: [{
                            required: true, message: '请再次输入密码!',
                        }, {
                            validator: this.compareToFirstPassword,
                        }],
                    })(
                        <Input prefix={<Icon type="lock" style={{color: 'rgba(0,0,0,.25)'}}/>} placeholder="请确认密码"
                               type="password" onBlur={this.handleConfirmBlur}/>
                    )}
                </Form.Item>
                <Form.Item
                    {...formItemLayout}
                    label="邮箱"
                >
                    {getFieldDecorator('email', {
                        rules: [{
                            type: 'email', message: '请输入合法邮箱!',
                        }, {
                            required: true, message: '请输入你的邮箱!',
                        }],
                    })(
                        <Input prefix={<Icon type="mail" style={{color: 'rgba(0,0,0,.25)'}}/>} placeholder="请输入邮箱"/>
                    )}
                </Form.Item>
                <Form.Item
                    {...formItemLayout}
                    label="手机号"
                >
                    {getFieldDecorator('contact', {
                        rules: [{required: true, message: '请输入手机号!'}],
                    })(
                        <Input prefix={<Icon type="phone" style={{color: 'rgba(0,0,0,.25)'}}/>} placeholder="请输入手机号"
                               addonBefore={prefixSelector} style={{width: '100%'}}/>
                    )}
                </Form.Item>
                <FormItem>
                    <a style={{float: 'right'}} className='register-form-register' onClick={() => {
                        this.state.history.replace('/login')
                    }}>去登录!</a>
                </FormItem>
                <FormItem className='register_button_wrapper'>
                    <Button type="primary" htmlType="submit" className="login-form-button">
                        注 册
                    </Button>
                </FormItem>
            </Form>
        )
    }
}

RegisterForm.propTypes = {
    handleRegister: PropTypes.func.isRequired
}

RegisterForm = Form.create({name: 'user_register'})(RegisterForm)


export default withRouter(Register)