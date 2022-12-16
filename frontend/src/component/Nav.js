import React, {useState, useEffect, useMemo, useCallback} from 'react';
import {NavLink} from "react-router-dom";
import styles from '../styles/nav/nav.module.css'

const Nav = () => {
    return (
        <div className={styles.outerNavbar}>
            {/*<a className={styles.logo}>*/}
            {/*    코끼리 사진*/}
            {/*</a>*/}
            <nav>
                <ul>
                    <li><NavLink to="/">Home </NavLink></li>
                    <li><NavLink to="/mulmultrade">
                        물물교환
                    </NavLink></li>
                    <li><NavLink to="/kirikiritrade">
                        끼리끼리
                    </NavLink></li>
                    <li><NavLink to="/mypage">
                        마이페이지
                    </NavLink></li>
                </ul>

            </nav>
        </div>
    );
};

export default Nav;