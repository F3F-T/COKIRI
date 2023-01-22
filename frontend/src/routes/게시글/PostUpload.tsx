import React, {useState, useEffect, useMemo, useCallback, useRef} from 'react';
import styles from "../../styles/loginAndSignup/PostUpload.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import photo from "../../img/add.png"
import PriceBox from "../../component/trade/PriceBox";
import Tags from "@yaireo/tagify/dist/react.tagify" // React-wrapper file
// import "@yaireo/tagify/src/tagify.scss"
import "../../styles/scss/main.scss"
import Button from "../../component/common/Button";
import Api from "../../utils/api";
import {setToken} from "../../store/jwtTokenReducer";
import {setUserInfo} from "../../store/userInfoReducer";
import {Rootstate, store} from "../../index";
import {useSelector} from "react-redux";
import axios from "axios/index";
//https://github.com/yairEO/tagify 에서 tagify 참조
import { NumericFormat } from 'react-number-format';
import Select from "react-select";

const PostUpload = () => {

    interface UploadData {
        title: string;
        price: string;
        content: string;
        productCategory: String;
        wishCategory: String;
        tag : string[];
    }

    const [uploadData, setUploadData] = useState<UploadData>()
    let tradeEachOther : boolean = undefined;
    const store = useSelector((state:Rootstate) => state);
    const fileInput = useRef(null)
    const [photoData,setPhotoData] = useState(null)
    const [photoUrl, setPhotoUrl] = useState<string[]>(null);

    // const categories: string[] =
    //     ['전체', '도서', '생활가전', '의류', '유아도서', '유아동', '여성의류', '남성의류', '뷰티/미용', '스포츠/레저',
    //         '티켓/교환권', '식물', '가구', '반려동물용품', '가공용품', '취미/게임', '인테리어', '생활/주방']

    interface Category {
        name: string;
    }
    
    const categories : Category[] = 
        [
            {name : '전체'}, 
            {name : '도서'},
            {name : '생활가전'},
            {name : '의류'}, 
            {name : '유아도서'}, 
            {name : '유아동'}, 
            {name : '여성의류'}, 
            {name : '남성의류'}, 
            {name : '스포츠/레저'}, 
            {name : '티켓/교환권'}, 
            {name : '식물'}, 
            {name : '가구'}, 
            {name : '반려동물용품'}, 
            {name : '가공용품'}, 
            {name : '취미/게임'}, 
            {name : '인테리어'}, 
            {name : '생활/주방'}]
    

    const navigate = useNavigate();
    const [showImages,setShowImages] = useState([]);

    const onChangeTitle = (e) => {
        const inputTitle = e.target.value;
        setUploadData((prevState) => {
            return {...prevState, title: inputTitle}
        })
    }

    const onChangePrice = (e,value) => {
        const inputPrice = e.target.value;
        console.log(inputPrice);


        setUploadData((prevState) => {
            return {...prevState, price: inputPrice}
        })
    }

    const onChangeContent = (e) => {
        const inputContent = e.target.value;
        setUploadData((prevState) => {
            return {...prevState, content: inputContent}
        })
    }

    const onChangeTag = (e) => {
        const inputTag = e.target.value;
    }

    const signUpButtonClick = () => {
        navigate(`/signup/emailcheck`)
    }

    // on tag add/edit/remove
    const onChange = useCallback((e) => {

        const tagifyCleanValue = e.detail.tagify.getCleanValue()

        let tagList = tagifyCleanValue.reduce((prev,cur)=>{
            prev.push(cur.value)
            return prev;
        },[]);

        console.log(tagList);

        setUploadData((prevState) => {
            return {...prevState, tag: tagList}
        })

    }, [])

    async function uploadPost(jsonObj) {

        //interceptor를 사용한 방식 (header에 token값 전달)
        try{
            console.log(jsonObj)
            const res = await Api.post('/post',jsonObj);
            console.log(res)

            alert("업로드 성공")
            navigate(`/`)
        }
        catch (err)
        {
            console.log(err)
            alert("업로드 실패")
        }


    }

    const imageUpload = async () => {
        try {
            const res = await Api.post("/auth/image/postImage", photoData);
            return res.data.imageUrls;

        } catch (err) {
            console.log(err)
            alert("이미지 업로드 실패")
        }


    }

    const onClickUploadButton = async() => {
        console.log(uploadData);

        if(uploadData.productCategory === uploadData.wishCategory)
        {
            tradeEachOther = true;

            console.log(tradeEachOther);
            console.log("true")
        }else{
            tradeEachOther = false;
            console.log(tradeEachOther);
            console.log("false")
        }



        //사진 업로드
        const photoUrlList = await imageUpload();
        if(!photoUrlList)
        {
            console.log("image 저장중")
            return null;
        }
        else{
        console.log(photoUrlList);
            const jsonObj = {
                "title": uploadData.title,
                "content":uploadData.content,
                "price":uploadData.price,
                "tradeEachOther": tradeEachOther,
                "authorId": store.userInfoReducer.id,
                "productCategory" : uploadData.productCategory,
                "wishCategory" : uploadData.wishCategory,
                "tagNames" : [...uploadData.tag],
                "images" : [...photoUrlList],
                "thumbnail" : photoUrlList[0]
            };
            uploadPost(jsonObj);
            console.log("업로드 성공")
        }



    }

    const onChangeImg = async(e: React.ChangeEvent<HTMLInputElement>) => {
        e.preventDefault();

        if(e.target.files){
            const imageLists = e.target.files;
            let imageUrlLists = [...showImages];
            const formData = new FormData()

            for(let i =0; i< imageLists.length ; i++)
            {
                //미리보기 파일 imgeUrlLists에 추가
                const currentImageUrl = URL.createObjectURL(imageLists[i]);
                imageUrlLists.push(currentImageUrl);
                //API 통신을 위한 formData에도 추가
                formData.append("imageFiles",imageLists[i]);
            }


            //max를 10장으로 설정
            if(imageUrlLists.length > 10)
            {
                imageUrlLists = imageUrlLists.slice(0,10)
                alert("사진 업로드는 최대 10장입니다.")
            }

            //미리보기 데이터
            setShowImages(imageUrlLists);
            console.log(showImages);

            //api 통신
            setPhotoData(formData);

            console.log(photoData);
            // console.log(uploadFile)
            // const formData = new FormData()
            // formData.append('imageFiles',uploadFile);
            // console.log(res);
            // setPhotoData(prevState => [...res.data.imageUrls])
        }
    }
    interface ArrayObjectSelectState {
        selectedCategory: Category | null;
    }

    const [productState, setProductState] = React.useState<ArrayObjectSelectState>({
        selectedCategory: null,
    });

    const [wishState, setWishState] = React.useState<ArrayObjectSelectState>({
        selectedCategory: null,
    });


    const onChangePriceSecond = (value) => {
        setUploadData((prevState) => {
            return {...prevState, price: value}
        })
    }

    /**
     * 로그인 상태를 확인하고 비로그인일때 이전화면으로 돌아가기
     */
    useEffect(()=>{
        if(store.jwtTokenReducer.authenticated)
        {
            console.log("로그인 상태")
        }
        else
        {
            alert("로그인후에 가능한 서비스입니다.")
            navigate(-1);
        }
    },[store.jwtTokenReducer.authenticated])


    return (
        <div className={styles.postBox}>
        <div className={styles.postUpload}>
            <div className={styles.header}>
                <p className={styles.header_1}>기본 정보</p>
                <p className={styles.header_2}>* 필수 정보</p>
            </div>
            <div className={styles.container}>
                <div className={styles.item1}>
                    <img className={styles.photos} src={photo} onClick={()=>{fileInput.current.click()}}/>
                    {
                        showImages.map((image,id)=>(
                                <img className={styles.photos} alt={`${image}-${id}`} key={id} src={image}/>
                        ))
                    }
                    <form>
                        <input type="file" style={{display:'none'}} multiple accept="image/*" onChange={onChangeImg} ref={fileInput}/>
                    </form>
                    <p className={styles.photoText}>최대 10장의 사진을 등록해주세요.</p>
                </div>
                <div className={styles.item2}>
                    <p className={styles.star}>*</p><input type="text" className={styles.item2_2} placeholder="글 제목을 적어주세요." onBlur={onChangeTitle} />
                </div>
                <div className={styles.item2}>
                    <p className={styles.star}>*</p> <NumericFormat className={styles.item2_2} placeholder="생각하는 물건의 가격대를 숫자로 적어주세요." prefix={"₩"} allowLeadingZeros thousandSeparator=","
                                                                    onValueChange={(values) => {
                                                                        onChangePriceSecond(values.floatValue);
                                                                    }} />
                </div>
                <div className={styles.contentAreaBox}>
                    <p className={styles.star}>*</p>
                    <textarea className={styles.contentArea} placeholder="상도1동에 올릴 게시글을 적어주세요." onBlur={onChangeContent} spellCheck={"false"}/>
                </div>
                <div className={styles.categoryBox}>
                    <p className={styles.star}>*</p>
                    <p className={styles.categoryText} >올릴 물건의 카테고리를 선택해주세요.</p>
                </div>
                <div className={styles.categoryBox2}>
                    <Select
                        styles={{ // zIndex
                            menu: provided => ({...provided, zIndex: 999})
                        }}
                        // If you don't need a state you can remove the two following lines value & onChange
                        value={productState.selectedCategory}
                        onChange={(option: Category | null) => {
                            setUploadData((prevState) => {
                                return {...prevState, productCategory: option.name}
                            })
                            setProductState({ selectedCategory: option });
                        }}
                        getOptionLabel={(category: Category) => category.name}
                        getOptionValue={(category: Category) => category.name}
                        options={categories}
                        isClearable={true}
                        backspaceRemovesValue={true}
                        placeholder={"전체"}
                    />

                </div>
                <div className={styles.categoryBox}>
                    <p className={styles.star}>*</p>
                    <p className={styles.categoryText2}>원하는 물건의 카테고리를 선택해주세요.</p>
                </div>
                <div className={styles.categoryBox2}>
                    <Select
                        styles={{ // zIndex
                            menu: provided => ({...provided, zIndex: 999})
                        }}
                        // If you don't need a state you can remove the two following lines value & onChange
                        value={wishState.selectedCategory}
                        onChange={(option: Category | null) => {
                            setUploadData((prevState) => {
                                return {...prevState, wishCategory: option.name}
                            })
                            setWishState({ selectedCategory: option });
                        }}
                        getOptionLabel={(category: Category) => category.name}
                        getOptionValue={(category: Category) => category.name}
                        options={categories}
                        isClearable={true}
                        backspaceRemovesValue={true}
                        placeholder={"전체"}
                    />

                </div>

                    <Tags
                        className={styles.customLook}
                        placeholder="해시태그를 적고 엔터를 눌러주세요."
                        //여기서 자동완성을 설정할수있음, 추후에 서버에서 tag 리스트를 가져와서 넣으면 될듯
                        whitelist={["스팸","식품","과일존맛","신상품","스팸클래식","이게자동완성이라는건데요"]}
                        // defaultValue="a,b,c"
                        onChange={onChange}
                    />



            </div>

            <div className={styles.btnPlace}>
                <button className={styles.uploadBtn} onClick={onClickUploadButton}>내 물건 올리기</button>
            </div>

        </div>

        </div>


    );

}

export default PostUpload;