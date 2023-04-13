import React, { Fragment } from "react";
import Table from 'react-bootstrap/Table';

export const TableOrderList = props => {

    const { tableTitleList, listData, setDataID } = props;

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
                                        if (element[propertyName] === "CLOSED") {
                                            return (
                                                <td key={index}><span style={{ color: '#006442', fontWeight: "bold" }}>{element[propertyName]}</span></td>
                                            )
                                        } else if (element[propertyName] === "PENDING") {
                                            return (
                                                <td key={index}><span style={{ color: '#FF8C00', fontWeight: "bold" }}>{element[propertyName]}</span></td>
                                            )
                                        } else if (element[propertyName] === "CANCEL") {
                                            return (
                                                <td key={index}><span style={{ color: 'red', fontWeight: "bold" }}>{element[propertyName]}</span></td>
                                            )
                                        }
                                        else {
                                            return (
                                                <td key={index}>{element[propertyName]}</td>
                                            )
                                        }
                                    })
                                }
                                <td key={'buttonRow'}>
                                    <button className="btn btn-success" onClick={() => { setDataID(element.id); }}>View Details</button>
                                </td>
                            </tr>
                        )
                    })}
                </tbody>
            </Table>
        </Fragment>
    );
} 