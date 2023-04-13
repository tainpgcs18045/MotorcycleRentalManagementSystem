import React, { useState, Fragment, useEffect } from "react";
import { Navbar, Nav } from "react-bootstrap";
import { styled } from '@mui/material/styles';
import {
    Badge,
    Divider,
    IconButton,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    Drawer
} from "@material-ui/core";
import ListItemButton from '@mui/material/ListItemButton';
import Collapse from '@mui/material/Collapse';
import DashboardIcon from '@mui/icons-material/Dashboard';
import TwoWheelerIcon from '@mui/icons-material/TwoWheeler';
import MopedIcon from '@mui/icons-material/Moped';
import CategoryIcon from '@mui/icons-material/Category';
import InvertColorsIcon from '@mui/icons-material/InvertColors';
import FactoryIcon from '@mui/icons-material/Factory';
import ManageSearchIcon from '@mui/icons-material/ManageSearch';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import ViewListIcon from '@mui/icons-material/ViewList';
import SettingsIcon from '@mui/icons-material/Settings';
import MenuIcon from '@mui/icons-material/Menu';
import CloseIcon from '@mui/icons-material/Close';
import AccountCircle from '@mui/icons-material/AccountCircle';
import ExpandLess from '@mui/icons-material/ExpandLess';
import ExpandMore from '@mui/icons-material/ExpandMore';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';
import Menu from '@mui/material/Menu';
import Cookies from 'universal-cookie';

// Redux
import { useDispatch } from "react-redux";
import { reduxAuthenticateAction } from "../../redux-store/redux/reduxAuthenticate.slice";


const cookies = new Cookies();

const DrawerHeader = styled('div')(({ theme }) => ({
    display: 'flex',
    alignItems: 'center',
    padding: theme.spacing(0, 1),
    ...theme.mixins.toolbar,
    justifyContent: 'flex-end',
}));

const Sidebar = () => {
    const dispatch = useDispatch();
    const [open, setOpen] = useState(false);
    const [managementBikeCollapse, setManagementBikeCollapse] = useState(false);
    const [managementOrderCollapse, setManagementOrderCollapse] = useState(false);
    const [anchorEl, setAnchorEl] = useState(null);

    let userName = cookies.get('userName');

    const toggleSlider = () => {
        setOpen(!open);
    };

    const toggleManagementBike = () => {
        setManagementBikeCollapse(!managementBikeCollapse);
    }

    const toggleManagementOrder = () => {
        setManagementOrderCollapse(!managementOrderCollapse);
    }

    const handleLogOut = () => {
        cookies.remove('accessToken');
        cookies.remove('userName');
        dispatch(reduxAuthenticateAction.updateToken(null));
        dispatch(reduxAuthenticateAction.updateIsShowPublicNavBar(true));
        setTimeout(() => {
            window.location.replace('/signin');
        }, 500);
    }

    const handleMenu = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    return (
        <Fragment>
            <Navbar key="lg" expand="lg" className="navbar-light px-3 sidebar"
                sticky="top" bg="light" role="navigation" collapseOnSelect
                style={{ justifyContent: "space-between" }}>
                <div style={{ display: "flex", alignItems: "center" }}>
                    <IconButton
                        color="default"
                        aria-label="open drawer"
                        onClick={toggleSlider}
                        edge="start"
                        style={{ ...(open && { display: 'none' }) }}
                    >
                        <MenuIcon />
                    </IconButton>
                    <Navbar.Brand href="/dashboard">Rent Motorcycles</Navbar.Brand>
                </div>
                <div>
                    <Button className="btn-user" variant="outlined" onClick={handleMenu} startIcon={<AccountCircle style={{ fontSize: '32px' }} />}>{userName}</Button>
                    <Menu
                        style={{ marginTop: '40px' }}
                        id="menu-appbar"
                        anchorEl={anchorEl}
                        anchorOrigin={{
                            vertical: 'top',
                            horizontal: 'right',
                        }}
                        keepMounted
                        transformOrigin={{
                            vertical: 'top',
                            horizontal: 'right',
                        }}
                        open={Boolean(anchorEl)}
                        onClose={handleClose}
                    >
                        {/* <MenuItem className="user-menu" onClick={handleClose}>Profile</MenuItem> */}
                        <MenuItem className="user-menu" onClick={() => handleLogOut()}>Log out</MenuItem>
                    </Menu>
                </div>
            </Navbar>
            <Drawer
                variant="persistent"
                anchor="left"
                open={open}
            >
                <DrawerHeader>
                    <IconButton onClick={toggleSlider}>
                        <CloseIcon />
                    </IconButton>
                </DrawerHeader>
                <Divider />
                <Nav>
                    <List
                        style={{ width: '100%', maxWidth: 500, minWidth: 400 }}
                        component="nav"
                        aria-labelledby="nested-list-subheader">
                        <ListItem key={'dashboard'} disableGutters={true}>
                            <ListItemButton className="item-button" component="a" href="/dashboard">
                                <ListItemIcon>
                                    <DashboardIcon className="item-icon" />
                                </ListItemIcon>
                                <ListItemText primary="Dashboard" />
                            </ListItemButton>
                        </ListItem>
                        <ListItem key={'bikeManagement'} disableGutters={true}>
                            <ListItemButton className="item-button" onClick={toggleManagementBike}>
                                <ListItemIcon>
                                    <TwoWheelerIcon className="item-icon" />
                                </ListItemIcon>
                                <ListItemText primary="Bike Management" />
                                {managementBikeCollapse ? <ExpandLess /> : <ExpandMore />}
                            </ListItemButton>
                        </ListItem>
                        <Collapse in={managementBikeCollapse} timeout="auto" unmountOnExit>
                            <List component="div" disablePadding>
                                <ListItem key={'bike'} disableGutters={true} style={{ paddingLeft: 40 }}>
                                    <ListItemButton className="item-button" component="a" href="/manage-bike/bike-list">
                                        <ListItemIcon>
                                            <MopedIcon className="item-icon" />
                                        </ListItemIcon>
                                        <ListItemText primary="Bike List" />
                                    </ListItemButton>
                                </ListItem>
                                <ListItem key={'category'} disableGutters={true} style={{ paddingLeft: 40 }}>
                                    <ListItemButton className="item-button" component="a" href="/manage-bike/category">
                                        <ListItemIcon>
                                            <CategoryIcon className="item-icon" />
                                        </ListItemIcon>
                                        <ListItemText primary="Category" />
                                    </ListItemButton>
                                </ListItem>
                                <ListItem key={'color'} disableGutters={true} style={{ paddingLeft: 40 }}>
                                    <ListItemButton className="item-button" component="a" href="/manage-bike/color">
                                        <ListItemIcon>
                                            <InvertColorsIcon className="item-icon" />
                                        </ListItemIcon>
                                        <ListItemText primary="Color" />
                                    </ListItemButton>
                                </ListItem>
                                <ListItem key={'manufacturer'} disableGutters={true} style={{ paddingLeft: 40 }}>
                                    <ListItemButton className="item-button" component="a" href="/manage-bike/manufacturer">
                                        <ListItemIcon>
                                            <FactoryIcon className="item-icon" />
                                        </ListItemIcon>
                                        <ListItemText primary="Manufacturer" />
                                    </ListItemButton>
                                </ListItem>
                            </List>
                        </Collapse>

                        <ListItem key={'orderManagement'} disableGutters={true}>
                            <ListItemButton className="item-button" onClick={toggleManagementOrder}>
                                <ListItemIcon>
                                    <ManageSearchIcon className="item-icon" />
                                </ListItemIcon>
                                <ListItemText primary="Order Management" />
                                {managementOrderCollapse ? <ExpandLess /> : <ExpandMore />}
                            </ListItemButton>
                        </ListItem>
                        <Collapse in={managementOrderCollapse} timeout="auto" unmountOnExit>
                            <List component="div" disablePadding>
                                <ListItem key={'cart'} disableGutters={true} style={{ paddingLeft: 40 }}>
                                    <ListItemButton className="item-button" component="a" href="/manage-order/cart-create">
                                        <ListItemIcon>
                                            <AddShoppingCartIcon className="item-icon" />
                                        </ListItemIcon>
                                        <ListItemText primary="Create Cart" />
                                    </ListItemButton>
                                </ListItem>

                                <ListItem key={'order'} disableGutters={true} style={{ paddingLeft: 40 }}>
                                    <ListItemButton className="item-button" component="a" href="/manage-order/order-list">
                                        <ListItemIcon>
                                            <ViewListIcon className="item-icon" />
                                        </ListItemIcon>
                                        <ListItemText primary="Order List" />
                                    </ListItemButton>
                                </ListItem>
                            </List>
                        </Collapse>
                        <ListItem key={'maintain'} disableGutters={true}>
                            <ListItemButton className="item-button" component="a" href="/manage-maintenance/maintenance-list">
                                <ListItemIcon>
                                    <SettingsIcon className="item-icon" />
                                </ListItemIcon>
                                <ListItemText primary="Maintenance" />
                            </ListItemButton>
                        </ListItem>
                    </List>
                </Nav>
            </Drawer>
        </Fragment>
    );
};

export default Sidebar;