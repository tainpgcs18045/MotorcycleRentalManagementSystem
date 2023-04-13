import React, { Fragment, useEffect, useState } from 'react';

// Redux
import { useSelector, useDispatch } from "react-redux";
import { reduxPaginationAction } from '../../redux-store/redux/reduxPagination.slice';

// Library
import { Pagination, TablePagination } from "@mui/material";
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

export const PaginationCustom = props => {

    const { totalPages } = props;
    const dispatch = useDispatch();

    const reduxPage = useSelector((state) => state.reduxPagination.page);
    const reduxRowsPerPage = useSelector((state) => state.reduxPagination.rowsPerPage);

    const handleChangePage = (event, newPage) => {
        dispatch(reduxPaginationAction.updatePage(newPage));
    };

    const handleChangeRowsPerPage = (event) => {
        dispatch(reduxPaginationAction.updateRowsPerPage(parseInt(event.target.value, 10)));
    };

    return (
        <Fragment>
            {props.isShowRowPerPage === false &&
                <Row>
                    <Col lg={12} xs={12} style={{ paddingTop: "10px" }}>
                        <Pagination
                            onChange={handleChangePage}
                            count={totalPages}
                            page={reduxPage}
                            color='primary'
                            variant='outlined'
                            shape='rounded'
                            showFirstButton={true}
                            showLastButton={true}
                        />
                    </Col>
                </Row>
            }
            {props.isShowRowPerPage !== false &&
                <Row>
                    <Col lg={9} xs={12} style={{ paddingTop: "10px" }}>
                        <Pagination
                            onChange={handleChangePage}
                            count={totalPages}
                            page={reduxPage}
                            color='primary'
                            variant='outlined'
                            shape='rounded'
                            showFirstButton={true}
                            showLastButton={true}
                        />
                    </Col>

                    <Col lg={3} xs={12}>
                        <TablePagination
                            rowsPerPageOptions={[5, 10, 25]}
                            rowsPerPage={reduxRowsPerPage}
                            component='div'
                            count={-1}
                            page={reduxPage}
                            onPageChange={handleChangePage}
                            onRowsPerPageChange={handleChangeRowsPerPage}
                            ActionsComponent={() => {
                                return <Fragment />;
                            }}
                            labelDisplayedRows={() => {
                                return <Fragment />;
                            }}
                        />
                    </Col>
                </Row>
            }
        </Fragment>
    );
} 