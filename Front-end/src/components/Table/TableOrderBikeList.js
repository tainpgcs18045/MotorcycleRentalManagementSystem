import React, { Fragment } from "react";
import Table from 'react-bootstrap/Table';

export const TableOrderBikeList = props => {

    const { tableTitleList, listData, setDataID, setIsDelete, isShowButtonDelete } = props;

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
                        {isShowButtonDelete === true &&
                            <th key={'buttonColumn'} style={{ width: '20%' }}>ACTION</th>
                        }
                    </tr>
                </thead>
                <tbody>
                    {listData.map((element) => {
                        return (
                            <tr key={element.id}>
                                {
                                    Object.keys(element).map(function (propertyName, index) {
                                        if (element[propertyName] === "AVAILABLE") {
                                            return (
                                                <td key={index}><span style={{ color: '#006442', fontWeight: "bold" }}>{element[propertyName]}</span></td>
                                            )
                                        } else if (element[propertyName] === "HIRED") {
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
                                {isShowButtonDelete === true &&
                                    <td key={'buttonRow'}>
                                        <button className="btn btn-danger" type="button" onClick={() => { setDataID(element.id); setIsDelete(true) }}>Delete</button>
                                    </td>
                                }

                            </tr>
                        )
                    })}
                </tbody>
            </Table>
        </Fragment>
    );
} 