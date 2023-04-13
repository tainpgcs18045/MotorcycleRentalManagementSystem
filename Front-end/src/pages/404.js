import React, { useState } from 'react';
import '../styles/style.scss';
import Footer from '../components/Footer/Footer';

// Redux
import { useDispatch } from "react-redux";
import { reduxAuthenticateAction } from '../redux-store/redux/reduxAuthenticate.slice';

const PageNotFound = props => {

    // Show Public Navigation
    const dispatch = useDispatch();
    const [loadingPage, setLoadingPage] = useState(true);
    if (loadingPage === true) {
        dispatch(reduxAuthenticateAction.updateIsShowPublicNavBar(true));
        setLoadingPage(false);
    }

    return (
        <div className='page-404'>
            <h1>404 - Page Not Found!</h1>
            <h2>{props.warn}</h2>
        </div>
    )
};

export default PageNotFound;