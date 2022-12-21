import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/trade/PostContainer.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../../img/cokkiriLogo.png"


// ㅂㅅ
const PostContainer = () => {

        let navigate = useNavigate();


        return (
        <div className={styles.postContainer}>
            <div className={styles.postItem}><img className={styles.postImage} src = {myImage} onClick={()=>navigate('/post')}/></div>
            <div className={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
            <div className={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
            <div className={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
            <div className={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
            <div className={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
            <div className={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
            <div className={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
            <div className={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
            <div className={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
            <div className={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
            <div className={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
            <div className={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
            <div className={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
            <div className={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
            <div className={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
        </div>
    );
}

export default PostContainer;