package org.zerock.myapp.entity;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

//@Value	// 읽기전용객체를 만들때
@Data		// 수정가능한 객체
//@Entity
@Entity // 이 클래스가 JPA가 매핑할 관계형 테이블로 매핑될 클래스임

//만일, 이 엔티티 클래스이름과 실제 생성될 테이블명이 틀릴 경우,
// 이 어노테이션으로 다른 이름을 매핑할 수 있습니다.
//@Table(name = "MEMBER")  // MEMBER -> MEMBER 로 매핑합니다.
// 엔티티 클래스는 우리가 배운대로 자바빈즈 스펙에 맞게 선언해야 합니다.
public class Member implements Serializable{
	@Serial private static final long serialVersionUID = 1L;
	
	@Id
	// PK 선언
	private String id;			// 키속성
	
	private String userName;	// 일반속성
	private Integer age;		// 일반속성

} // end class
