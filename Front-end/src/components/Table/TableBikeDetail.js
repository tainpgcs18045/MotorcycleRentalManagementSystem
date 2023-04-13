import React, { Fragment } from "react";
import Table from 'react-bootstrap/Table';
import { GetFormattedCurrency } from "../../function/CurrencyFormat";

export const TableBikeDetail = props => {
    const { bikePrice } = props;

    return (
        <Fragment>
            <Table responsive bordered style={{ border: '2px solid', textAlign: 'center' }}>
                <thead>
                    <tr key={0}>
                        <th key={'ToRentTime'} style={{ width: '25%' }}>Exceed hours</th>
                        <th key={'PriceToRent'} style={{ width: '25%' }}>Extra fee</th>
                        <th key={'ProcedureToRent'} style={{ width: '25%' }}>Procedure to rent</th>
                        <th key={'DownPayment'} style={{ width: '25%' }}>Down payment</th>
                    </tr>
                </thead>
                <tbody>
                    <tr key={1}>
                        <td key={'1.1'}>Less than 1 hour</td>
                        <td key={'1.2'}>
                            <p>{GetFormattedCurrency(bikePrice * 0)}</p>
                            <p>(Coefficient: 0)</p>
                        </td>
                        <td rowSpan="3" key={'1.3'}>
                            <p>The following photo identification documents will be retained by the company (in original form):</p>
                            <p style={{ marginTop: '16px', fontWeight: 600, color: '#1F4788' }}>Identified card , GPLX , Passport</p>
                        </td>
                        <td rowSpan="3" key={'1.4'}>
                            <p style={{ fontWeight: 600, color: '#ff4444' }}>If you do not have identification documents, you must pay a deposit in order to create a contract.</p>
                            <p style={{ marginTop: '16px' }}>The deposit will be determined by the quantity and value of the vehicle.</p>
                            <p style={{ marginTop: '16px', fontWeight: 600, color: '#1F4788' }}>Refund of the deposit at the end of the lease</p>
                        </td>
                    </tr>
                    <tr key={2}>
                        <td key={'2.1'}>Less than 7 hours</td>
                        <td key={'2.2'}>
                            <p>{GetFormattedCurrency(bikePrice * 0.5)}</p>
                            <p>(Coefficient: 0.5)</p>
                        </td>
                    </tr>
                    <tr key={3}>
                        <td key={'3.1'}>Equals or is greater than 7 hours</td>
                        <td key={'3.2'}>
                            <p>{GetFormattedCurrency(bikePrice * 1)}</p>
                            <p>(Coefficient: 1)</p>
                        </td>
                    </tr>
                </tbody>
            </Table>
        </Fragment>
    );
}