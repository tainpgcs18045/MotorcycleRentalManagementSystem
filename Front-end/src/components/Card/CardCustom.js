import React from "react";

// Library
import Card from '@mui/material/Card';
import CardHeader from '@mui/material/CardHeader';
import CardContent from '@mui/material/CardContent';
import AnimatedNumber from "animated-number-react";
import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';

export const CardCustom = (props) => {

    const { icon, title, value, setFormatValue, colorType, duration } = props;

    return (
        <Card variant="outlined" className={'card-custom card-custom-' + colorType} style={{ display: 'flex' }}>
            <Box className="card-icon">
                <Avatar className={'card-icon-avatar card-icon-avatar-' + colorType}>
                    {icon}
                </Avatar>
            </Box>
            <Box>
                <CardHeader title={title} className="card-custom-header" />
                <CardContent className="card-custom-content">
                    <AnimatedNumber
                        className={'card-value card-value-' + colorType}
                        value={value}
                        formatValue={(value) => setFormatValue(value)}
                        duration={duration}
                    />
                </CardContent>
            </Box>
        </Card >
    )
}