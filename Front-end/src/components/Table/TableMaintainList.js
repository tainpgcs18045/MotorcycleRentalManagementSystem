import React, { Fragment } from "react";
import Table from 'react-bootstrap/Table';
import { useNavigate } from "react-router-dom";

export const TableMaintainList = props => {

    const { setShowPopup, setTitlePopup, tableTitleList, listData, setDataID, setIsDelete } = props;

    const navigate = useNavigate();

    return (
        <Fragment>
            <Table responsive>
                <thead>
                    <tr>
                        {tableTitleList.map((element, index) => {
                            return (
                                <th key={index} style={{ width: element.width }}>{element.name}</th>
                            )
                        })}
                        <th key={'buttonColumn'} style={{ width: '20%' }}>ACTION</th>
                    </tr>
                </thead>
                <tbody>
                    {listData.map((element) => {
                        return (
                            <tr key={element.id}>
                                {
                                    Object.keys(element).map(function (propertyName, index) {
                                        return (
                                            <td key={index}>{element[propertyName]}</td>
                                        )
                                    })
                                }
                                <td key={'buttonRow'}>
                                    <button className="btn btn-success table-btn" type="button" onClick={() => navigate("/manage-maintenance/maintenance/" + element.id)}>View</button>
                                    <button className="btn btn-danger table-btn" type="button" onClick={() => { setShowPopup(true); setTitlePopup("Delete"); setDataID(element.id); setIsDelete(true) }}>Delete</button>
                                </td>
                            </tr>
                        )
                    })}
                </tbody>
            </Table>
        </Fragment>
    );
} 