import React, {useState, useEffect, useMemo, useCallback} from 'react';
import { NavLink } from "react-router-dom";
import '../styles/Nav.css'

const Nav = () => {
    return (
        <nav>
            <div>
                <NavLink to="/">
                    Home
                </NavLink>
            </div>
            <div>
                <NavLink to="/mulmultrade">
                    물물교환
                </NavLink>
            </div>
            <div>
                <NavLink to="/kirikiritrade">
                    끼리끼리
                </NavLink>
            </div>
            <div>
                <NavLink to="/mypage">
                    마이페이지
                </NavLink>
            </div>
        </nav>
    );
};

export default Nav;