import React, { useState } from 'react';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';

export const ButtonScroll = () => {
    const [visible, setVisible] = useState(false);

    const toggleVisible = () => {
        const scrolled = document.documentElement.scrollTop;
        if (scrolled > 30) {
            setVisible(true)
        }
        else if (scrolled <= 30) {
            setVisible(false)
        }
    };

    const scrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
            /* you can also use 'auto' behaviour
               in place of 'smooth' */
        });
    };

    window.addEventListener('scroll', toggleVisible);

    return (
        <Button>
            <KeyboardArrowUpIcon onClick={scrollToTop}
                style={{ display: visible ? 'inline' : 'none' }} />
        </Button>
    );
}