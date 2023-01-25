import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/talk/kokiriTalk.module.scss"
import profileImg from "../img/profileImg.png"
import spamImg from "../img/spam.png"
import Comments from "../component/comments/Comments";
import Card from "../component/tradeCard/Card";
import TalkList from "../component/talk/TalkList";
import {useLocation, useNavigate} from "react-router-dom";
import Message from "../component/talk/Message";
import TalkCard from "../component/talk/TalkCard";
import {HiPencil} from "react-icons/hi";
import Api from "../utils/api";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../index";
import {
    setMessageRoomId,
    setOpponetNick,
    setPostId,
    setSellerId,
    setBuyerId,
    setProductImg, setTitle, setWishCategory, setTradeCategory, setTradeStatus
} from "../store/talkCardReducer";
import {use} from "js-joda";
import {log} from "util";

interface contentInfo {
        id: number,
        senderNickname: string,
        receiverNickname: string,
        content: string,
        senderId: string,
        receiverId: string,
        messageRoomId: number
}
interface contentInfo2 {
    id: number,
    senderNickname: string,
    receiverNickname: string,
    content: string,
    senderId: string,
    receiverId: string,
    messageRoomId: number
}
const KokiriTalk = () => {
    const navigate = useNavigate();
    const [key,setKey] = useState<number>(null)
    const {state} = useLocation();
    console.log("이방이 이미 있는거니wwwwwwww???", state)
    const dispatch = useDispatch();
    const talkCard = useSelector((state : Rootstate)=>{return state.talkCardReducer})
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})
    const [roomId,setRoomId] = useState();
    const [contentInfo,setContentInfo]=useState(null)
    const [roomList,setRoomList]=useState(null)
    // const [count,setCount]=useState(0)

    const [input,setInput] = useState('');
    useEffect(()=>{
        getMessageRoom()
    },[])
    // console.log("제대로 가져왔나 확인",state.images[0].imgPath)


    // const onClickTalkList = () => {
    //     setKey(1);
    // }
    //
    // const onClickTalkList2 = () => {
    //     console.log("2번 클릭 이벤트");
    //     setKey(2);
    //
    // }
    //
    // const onClickTalkList3 = () => {
    //     console.log("3번 클릭 이벤트")
    //     setKey(3);
    // }

    const onClickTotalTalkList = (key) => {
        return (event: React.MouseEvent) => {
            setKey(key);
            event.preventDefault();
        }
    }
    console.log("이방이 이미 있는거니???", state)
    console.log("지금 담겨있는 리덕스 확인해보자",talkCard)
    const objectMessage = [{
        keys: 1,
        partner : "함민혁",
        lastContent : "얼마에 팔건가요????",
        date : "2020.1.2",
        message: ["111111보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸사람 : 이거 얼마", "받은 사람 : 삼천원"
        ]
    },
        {
            keys: 2,
            partner : "홍의성",
            lastContent : "주무시나요2",
            date :  "2020.1.2",
            message: ["22222보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸사람 : 이거 얼마", "받은 사람 : 삼천원"
            ]
        },
        {
            keys: 3,
            partner : "김희찬",
            lastContent : "주무시나요3",
            date :  "2020.1.2",
            message: ["3333333보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸사람 : 이거 얼마", "받은 사람 : 삼천원"
            ]
        }
    ]
    console.log("아ㅏㅏㅏㅏㅏㅏㅏ이ㅣㅣㅣㅣㅣㅣㅣ디ㅣㅣㅣㅣㅣ",state)
    // const setMessageRoomId = async () => {
    //     setRoomId(res.data.id)
    //     return roomId
    // }
    const onChangeMessage = (e) => {
        const inputMessage = e.target.value;
        setInput(inputMessage)
        return setInput
    }
    const createMessageRoom = async () => {
        console.log("이방이 이미 있는거니???", state)
        //얘는 게시글통해서 들어왔을때만 그 톡방이 있는지 확인하면됨
        if(state===false){
            try{
                const post_buyerId1 = {
                    postId: talkCard.postId,
                    buyerId: info.id
                }
                const res = await Api.post(`/post/${talkCard.postId}/messageRooms`,post_buyerId1);
                dispatch(setOpponetNick(res.data.sellerNickName))
                await dispatch(setMessageRoomId(res.data.id))
                await dispatch(setSellerId(res.data.sellerId))
                dispatch(setPostId(res.data.postId))

                setRoomId(res.data.id)
                console.log("메세지룸 추가", res.data)
                // if(talkCard.id != undefined){
                //     sendMessage()
                // }
                alert("메세지룸 추가 성공")
                await sendMessage(res.data.id)
            }
            catch (err)
            {
                console.log(err)
                alert("메세지룸 추가 실패")
            }

        }
        else{
            console.log("제발좀ㅋㅋ",talkCard.id)
            await sendMessage(talkCard.id)
        }

    }
    async function getMessageRoom() {
        try{
            const res = await Api.get('/user/messageRooms');
            console.log("메세지룸 조회", res.data.content)
            const res2 = await Api.get(`/user/${info.id}/totalMessageRooms`);
            console.log("메세지룸 조회2",res2.data)
            // dispatch(setRoomsId(res.data.content.))
            //첫번째 화면에서 첫번째 게시글을 띄우고싶으면 여기에다가 리덕스를 추가해
            // dispatch(setProductImg(res.data.content.))
            // dispatch(setTitle(post.title))
            // dispatch(setWishCategory(post.wishCategory))
            // dispatch(setTradeCategory(post.productCategory))
            // dispatch(setTradeStatus(post.tradeStatus))
            // dispatch(setSellerId(post.userInfoWithAddress.userDetail.id))


            // dispatch(setPostId(res2.data[0].postId))
            // dispatch(setMessageRoomId(res2.data[0].id))
            // dispatch(setBuyerId(res2.data[0].buyerId))
            // if(info.id === res2.data[0].buyerId){
            //     dispatch(setOpponetNick(res2.data[0].sellerNickName))
            // }
            // else{
            //     dispatch(setOpponetNick(res2.data[0].buyerNickName))
            // }

            setRoomList(()=>{
                return [...res.data.content]
            })

            getMessageContent(res2.data[0].id);

            alert("메세지룸 조회 성공in kokiritalk")

            return res2.data[0].id
        }
        catch (err)
        {
            console.log(err)
            alert("메세지룸 조회 실패")
        }
    }
    async function sendMessage(loading) {
        let messageInfo1={
            content:input,
            senderId:undefined,
            receiverId:undefined,
            postId:undefined,
            messageRoomId:undefined
        }
        try{
            if(info.id===talkCard.sellerId){
                console.log("메세지보내는사람이 파는 사람이야")
                messageInfo1={
                    content:input,
                    senderId:info.id,
                    receiverId:talkCard.buyerId,
                    postId:talkCard.postId,
                    messageRoomId:loading
                }
                console.log("messageInfo1파는",messageInfo1)
            }
            else{
                console.log("메세지보내는사람이 사는 사람이야")
                messageInfo1={
                    content:input,
                    senderId:info.id,
                    receiverId:talkCard.sellerId,
                    postId:talkCard.postId,
                    messageRoomId:loading
                }
                console.log("messageInfo1사는",messageInfo1)

            }
            const res = await Api.post(`/messageRooms/${loading}`,messageInfo1);
            console.log("메세지 전송", res.data)
            alert("메세지 전송  성공")

        }
        catch (err)
        {
            console.log(err)
            alert("메세지전송  실패")
        }
    }
    // let contentInfo1 :{
    //     id: number,
    //     senderNickname: string,
    //     receiverNickname: string,
    //     content: string,
    //     senderId: string,
    //     receiverId: string,
    //     messageRoomId: number
    // }
    let contentInfo1=[]
    async function getMessageContent(loading) {
        try{
            const res = await Api.get(`/messageRooms/${loading}`);
            console.log("메세지룸 내용조회", res.data)
            setContentInfo(()=>{
                return [...res.data]
            })

            // for(let i=0;i<res.data.length;i++){
            //     contentInfo1[i]=res.data[i]
            // }
            // console.log("바로들어간다니까",contentInfo1)
            return res.data
        }
        catch (err)
        {
            console.log(err)
            alert("메세지룸 내용 조회 실패 in kokiritalk")
        }
    }
    // const load = await getMessageRoom();
    // const loading = await createMessageRoom();
    // useEffect(()=>{
    //     // getMessageContent(talkCard.id);
    //     getMessageContent(talkCard.id);
    // },[talkCard.id])
    // if(!talkCard.id){
    //     return null;
    // }
    // else{
    //     getMessageContent(talkCard.id);
    // }
    console.log("asdfasdfsdafsd",contentInfo)
    const hi = () => {
        console.log("contentInfo",contentInfo)
        // console.log("contentInfo1",contentInfo1[0].content)
        // contentInfo1.map((a,i)=>(
        //     console.log("contentInfo222",contentInfo1[i].senderId)
        // ))
    }

    // if(!contentInfo1){
    //     return null
    // }
    console.log("asdfa",contentInfo)

    if(!contentInfo){
        return null
    }
    if(!roomList){
        return null
    }

    console.log("렌더링",roomList)



//
    return (
        <div className={styles.kokiritalk}>
            <div className={styles.left}>
                <div className={styles.leftHeader}>코끼리톡</div>
                <div className={styles.left2}>
                <div className={styles.talkContainer}>
                 {/*<TalkList keys={1} partner={"함민혁"} lastContent={"주무시나요"} date={"몰라"} onClick = {onClickTalkList} />*/}
                 {/*<TalkList keys={2} partner={"홍의성"} lastContent={"주무시나요2"} date={"몰라"} onClick = {onClickTalkList2} />*/}
                 {/*<TalkList keys={3} partner={"함민혁"} lastContent={"주무시나요3"} date={"몰라"} onClick = {onClickTalkList3} />*/}
                 {/*   {objectMessage.map((SingleObject:object) => (*/}
                 {/*       <TalkList keys={SingleObject["keys"]} partner={SingleObject["partner"]} lastContent={SingleObject["lastContent"]} date={SingleObject["date"]}*/}
                 {/*                 onClick = {onClickTotalTalkList(SingleObject["keys"])} />*/}
                 {/*   ))}*/}
                    {roomList.map((SingleObject:object) => (
                        <TalkList keys={SingleObject["messageRoomId"]} partner={SingleObject["buyerId"]} lastContent={SingleObject["lastMsg"]} date={SingleObject["createdDated"]}
                                  onClick = {onClickTotalTalkList(SingleObject["messageRoomId"])} />
                    ))}
                </div>
                </div>
            </div>

            <div className={styles.right}>
                <div className={styles.right_headerBox}>
                    <div className={styles.right_header}>
                        <div className={styles.right_header1}>
                            <div className={styles.right_header1_1}> <TalkCard/> </div>
                        </div>
                        <div className={styles.right_header2}>
                            <p className={styles.delete}>삭제</p>
                            <p> | </p>
                            <p className={styles.block}>차단</p>
                            <p> | </p>
                            <p className={styles.inform}>신고</p>
                        </div>
                    </div>
                    <div className={styles.right_header1_2}>{talkCard.opponentNickname}님과의 쪽지방입니다.</div>
                </div>
                <div className={styles.talkContainer2}>
                    {/*{contentInfo.map((a,i)=>(*/}
                    {/*   contentInfo[i].senderId === info.id?*/}
                    {/*        <div className={styles.receive}>*/}
                    {/*            <div className={styles.receiveTitle}>보낸 쪽지</div>*/}
                    {/*            <p className={styles.receiveContent}>{contentInfo[i].content}</p>*/}
                    {/*        </div>*/}
                    {/*        :*/}
                    {/*        <div className={styles.send}>*/}
                    {/*            <div className={styles.sendTitle}>받은 쪽지</div>*/}
                    {/*            <p className={styles.sendContent}>{contentInfo[i].content}</p>*/}
                    {/*        </div>*/}
                    {/*))*/}
                    {/*}*/}
                    {/*<div className={styles.send}>*/}
                    {/*    <div className={styles.sendTitle}>받은 쪽지</div>*/}
                    {/*    <p className={styles.sendContent}>{contentInfo[0].content}</p>*/}
                    {/*</div>*/}
                    {key===null?
                        <></>:<Message keys={key}/>
                    }
                </div>
                <div className = {styles.writeComments}>
                    <input type={"text"} className={styles.writeInput} placeholder={"쪽지를 보내세요"} onChange={onChangeMessage}/>
                    <HiPencil className={styles.pencilIcon} onClick={()=>{createMessageRoom();}} />
                </div>
                <button onClick={hi}>버튼</button>
            </div>
        </div>
    );
}


export default KokiriTalk;