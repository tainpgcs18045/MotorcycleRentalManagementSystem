import React, { Fragment } from "react";
import { NavDropdown, Navbar, Nav, Container, Offcanvas } from "react-bootstrap";

const MenuBar = () => {
    return (
        <Fragment>
            <Navbar key="lg" expand="lg" className="navbar-light px-3 px-lg-5" sticky="top" bg="light" role="navigation" collapseOnSelect >
                <Container fluid>
                    <Navbar.Brand href="/">Rent Motorcycles</Navbar.Brand>
                    <Navbar.Toggle aria-controls={`offcanvasNavbar-expand-lg`} />
                    <Navbar.Offcanvas
                        id={`offcanvasNavbar-expand-lg`}
                        aria-labelledby={`offcanvasNavbarLabel-expand-lg`}
                        placement="end"
                    >
                        <Offcanvas.Header closeButton>
                            <Offcanvas.Title id={`offcanvasNavbarLabel-expand-lg`}>
                                Rent Motorcycles
                            </Offcanvas.Title>
                        </Offcanvas.Header>
                        <Offcanvas.Body>
                            <Nav className="me-auto" variant="pills">
                                <Nav.Link className="nav-item mx-lg-4" href="/">Home</Nav.Link>
                                {/* <Nav.Link className="nav-item mx-lg-4" href="/about-us">About Us</Nav.Link> */}
                                <NavDropdown
                                    title="List of motorbikes"
                                    id={`offcanvasNavbarDropdown-expand-lg`}
                                >
                                    <NavDropdown.Item href="/list/manual">Manual Transmission Motorcycle</NavDropdown.Item>
                                    <NavDropdown.Item href="/list/automatic">Automatic Transmission Motorcycle</NavDropdown.Item>
                                    <NavDropdown.Divider />
                                    <NavDropdown.Item href="/list">See all</NavDropdown.Item>
                                </NavDropdown>
                                {/* <Nav.Link className="nav-item mx-lg-4" href="/contact-us">Contact Us</Nav.Link> */}
                            </Nav>
                        </Offcanvas.Body>
                    </Navbar.Offcanvas>
                </Container>
            </Navbar>
        </Fragment>
    )
}

export default MenuBar;