import React from "react";

export const Banner = () => {
    return (
        <div className="banner d-none d-md-block"
            style={{
                background: "url(https://firebasestorage.googleapis.com/v0/b/bike-hiring-management-d7a01.appspot.com/o/web-image%2Fbanner.jpg?alt=media&token=02bb0c9b-8850-4989-9543-eb5975dc1572)",
                backgroundSize: "100% 100%",
                backgroundRepeat: "no-repeat",
                backgroundPosition: "0% 0%",
                backgroundAttachment: "scroll",
            }}
        >
            <div className="banner__info">
                <div className="banner__detail">
                    <h1>Do you want to hire a motorcycle?</h1>
                    <p>Learn about us now to find the right vehicle for you.</p>
                </div>
            </div>
        </div>
    )
}