import React, {Component} from 'react'
import {Menu, Avatar, Icon} from 'antd'
import './index.less'
import MenuConfig from '../../config/menuConfig'
import {NavLink} from 'react-router-dom'

const SubMenu = Menu.SubMenu

export default class NavLeft extends Component {

    componentWillMount() {
        const menuTreeNode = this.renderMenu(MenuConfig)
        // console.log(menuTreeNode)
        this.setState({
            avatar: sessionStorage.getItem('avatar'),
            menuTreeNode: menuTreeNode
        })
    }

    //菜单渲染
    renderMenu = (data) => {
        return data.map((item) => {
            const icon = item.icon
            if (item.children)
                return (
                    <SubMenu title={item.title} key={item.key}>
                        {this.renderMenu(item.children)}
                    </SubMenu>
                )
            return <Menu.Item title={item.title} key={item.key}><NavLink
                to={item.key}>{icon ? <Icon type={icon}/> : ''}{item.title}</NavLink></Menu.Item>
        })
    }

    render() {
        const {avatar} = this.state
        return <div>
            <div className='logo'>
                <Avatar size={"large"} src={avatar ? avatar : "/assets/logo-ant.svg"} alt=""/>
                <h1>组会管理平台</h1>
            </div>
            <Menu
                mode="inline"
                theme={'dark'}
            >
                {this.state.menuTreeNode}
            </Menu>
        </div>
    }

}