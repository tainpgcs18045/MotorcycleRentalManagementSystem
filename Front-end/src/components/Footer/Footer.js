import React from "react";
import LocationOnIcon from '@mui/icons-material/LocationOn';
import PhoneInTalkIcon from '@mui/icons-material/PhoneInTalk';
import EmailIcon from '@mui/icons-material/Email';

const Footer = () => {

    return (
        <div id="footer">
            <div className="row">
                <div className="col-lg-4">
                    <span>© Rent Motorcycles</span>
                </div>
                <div className="col-lg-8">
                    <ul>
                        <li>
                            <a href="#" className="info"><LocationOnIcon className="icon" /> Address: 6/3A Bui Thi Xuan Street - Phuoc Tien Ward - Nha Trang - Khanh Hoa</a>
                        </li>
                        <li>
                            <a href="tel:0766552288" className="info"><PhoneInTalkIcon className="icon" /> 07.66.55.22.88 (Ms. Be)</a>
                        </li>
                        <li>
                            <a href="tel:0913495175" className="info"><PhoneInTalkIcon className="icon" /> 0913.495.175 (Mr. Ro)</a>
                        </li>
                        <li>
                            <a href="#" className="info"><EmailIcon className="icon" /> hiremotorcycle@contact</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    )
}

export default Footer;