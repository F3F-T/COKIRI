import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/trade/PostContainer.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../../img/cokkiriLogo.png"
import Card from "../tradeCard/Card"
import Comments from "../comments/Comments";
import {storeCategory} from "../../store/categoryReducer";
import {useDispatch, useSelector} from "react-redux";
import {storePostDetail} from "../../store/postDetailReducer";
import TalkList from "../talk/TalkList";
import {Rootstate} from "../../index";
import tradeEx from "../../img/tradeEx.jpeg";
import transfer from "../../img/transfer.png";
import styled from "../../styles/card/cards.module.scss";
import Api from "../../utils/api";
import {setToken} from "../../store/jwtTokenReducer";
import {setUserInfo} from "../../store/userInfoReducer";
import {log} from "util";
import nav from "../Nav";

interface PostType {
    id? : number;
    title? : string;
    content? : string;
    tradeEachOther? : boolean;
    authorNickname? : string;
    wishCategory? : string;
    productCategory? : string;
    tradeStatus? : string;
    tagNames? : string[];
}


const PostContainer = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const detail = useSelector((state : Rootstate)=>{return state.postDetailReducer})

    const store = useSelector((state:Rootstate) => state);

    const [postList,setPostList] = useState<PostType[]>(null)
    const [loading,setLoading] = useState(false);
    async function getPostList() {

        //interceptor를 사용한 방식 (header에 token값 전달)
        try{
            const res = await Api.get('/post');
            console.log(res.data)

            setPostList(prevState => {
                return [...res.data];
            })

            console.log(postList);

        }
        catch (err)
        {
            console.log(err)
            alert("get 실패");
        }

    }


    // getPostList();
    useEffect(()=>{
        getPostList();
    },[])


    /**
     * 중요) postList를 async로 받긴 하지만 받아오는 시간 전까지는 postList가 null이기 때문에 밑에있는 render 에서 postList.map 이 null을 접근하게 돼서 오류가 발생하고, 켜지지 않는다
     * 이렇게 예외처리를 꼭 해야한다.
     */
    if (!postList) {
        return null
    }

    const onClickPost = (post) => {
        console.log(post)
        console.log(post.id)
        navigate(`/post/${post.id}`)
    }

        return (
        <div className={styles.postContainer}>
            {
                postList.map((post)=>(
                    <Card key = {post.id} className={"forTrade"} postTitle={post.title} postContent={post.content} wishCategory={post.wishCategory}
                          onClick={() => {onClickPost(post)}}/>

                ))
            }
        </div>
    );
}

export default PostContainer;