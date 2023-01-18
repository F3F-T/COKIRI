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
import Tags from "@yaireo/tagify/dist/react.tagify";
import Api from "../utils/api";
const Nav1 = () => {

    const store = useSelector((state:Rootstate) => state);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [tagSearch, setTagSearch] = useState<string[]>()


    const onChange = useCallback((e) => {

        const tagifyCleanValue = e.detail.tagify.getCleanValue()

        let tagList = tagifyCleanValue.reduce((prev,cur)=>{
            prev.push(cur.value)
            return prev;
        },[]);
//
        console.log(tagList);

        setTagSearch((prevState) => {
            return tagList
        })

    }, [])


    async function searchByTags(tag) {

        //interceptor를 사용한 방식 (header에 token값 전달)
        try{
            const res = await Api.get(`/post/tagSearch?tags=${tag}`);
            console.log(res)
            navigate('/tagsearch');

        }
        catch (err)
        {
            console.log(err)
            alert("실패") //
        }


    }

    const onClickSearch = () => {
        const tagString = tagSearch.join();
        searchByTags(tagString);
    }

    console.log(tagSearch)

    return (
        <div className={styles.navBarBar}>
        <Navbar className={styles.navBar} bg="white" variant="white">
            <img className={styles.homeLogo} onClick={()=>navigate('/')}  src = {myImage}/>
            <Nav className={styles.meauto}>
                <Nav.Link className={styles.mulBtn}  onClick={()=>navigate('/mulmultrade')}>물물교환</Nav.Link>
                <Nav.Link className={styles.kiriBtn}  onClick={()=>navigate('/kirikiritrade')}>끼리끼리</Nav.Link>
                {/*<form className={styles.searchBox}>*/}
                    {/*<input className={styles.search} type="search" placeholder=" #해시태그를 검색하세요!" aria-disabled="true"/>*/}
                    <Tags
                        className={styles.customLook}
                        placeholder="해시태그를 적고 엔터를 눌러주세요."
                        //여기서 자동완성을 설정할수있음, 추후에 서버에서 tag 리스트를 가져와서 넣으면 될듯
                        whitelist={["스팸","식품","과일존맛","신상품","스팸클래식","이게자동완성이라는건데요"]}
                        // defaultValue="a,b,c"
                        onChange={onChange}
                    />
                <button className={styles.signBtn} onClick={onClickSearch}>검색</button>
                {/*</form>*/}
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