import React, {useState, useEffect, useMemo, useCallback} from 'react';
import {NavLink, useNavigate} from "react-router-dom";
import styles from '../styles/nav/nav.module.css'
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../img/cokkiriLogo.png";
import mypage from "../img/mypage.png";
import talk from "../img/talk.png";
import Message from "./로그인 & 회원가입/Message";
import {Rootstate} from "../index";
import {useDispatch, useSelector} from "react-redux";
const Nav1 = () => {

    const store = useSelector((state:Rootstate) => state);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    return (
        <div className={styles.navBarBar}>
        <Navbar className={styles.navBar} bg="white" variant="white">
            <img className={styles.homeLogo} onClick={()=>navigate('/')}  src = {myImage}/>
            <Nav className={styles.meauto}>
                <Nav.Link className={styles.mulBtn}  onClick={()=>navigate('/mulmultrade')}>물물교환</Nav.Link>
                <Nav.Link className={styles.kiriBtn}  onClick={()=>navigate('/kirikiritrade')}>끼리끼리</Nav.Link>
                <form className={styles.searchBox}>
                    <input className={styles.search} type="search" placeholder=" #해시태그를 검색하세요!" aria-disabled="true"/>
                </form>
                {/*{(store.jwtTokenReducer.authenticated  ?*/}
                {/*    <button className={styles.signBtn} onClick={()=>navigate('/mypage')}>{store.userInfoReducer.nickname}님</button>*/}
                {/*    :*/}
                {/*    <button className={styles.signBtn} onClick={()=>navigate('/login')}>로그인/회원가입</button>)*/}
                {/*}*/}
                <div>
                <img className={styles.mypageI} onClick={()=>navigate('/')} src = {store.jwtTokenReducer.authenticated  ? mypage:mypage}/>
                {(store.jwtTokenReducer.authenticated  ?
                   <button className={styles.signBtn2} onClick={()=>navigate('/mypage')}>{store.userInfoReducer.nickname}님 상점</button>
                   :
                   <button className={styles.signBtn} onClick={()=>navigate('/login')}>로그인/회원가입</button>)
                }
                </div>
                <div>
                    <img className={styles.mypageI} onClick={()=>navigate('/')}  src = {talk}/>
                    <button className={styles.signBtn3} onClick={()=>navigate('/kokiritalk')}>코끼리 톡</button>
                </div>
            </Nav>

        </Navbar>
        </div>

    );
};

export default Nav1;