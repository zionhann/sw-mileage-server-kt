package edu.handong.cseemileage

import edu.handong.cseemileage.domain.acount.Admin
import edu.handong.cseemileage.domain.acount.Student
import edu.handong.cseemileage.dto.account.admin.AdminForm
import edu.handong.cseemileage.dto.account.student.StudentForm
import edu.handong.cseemileage.repository.account.AdminRepository
import edu.handong.cseemileage.repository.account.StudentRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class AuthService(
    val studentRepository: StudentRepository,
    val adminRepository: AdminRepository,
    val tokenProvider: TokenProvider
) {

    /**
     * 학생은 sid로 기존 계정 존재 여부 확인
     * 관리자는 직번/이메일 활용 -> 어떻게 확인할 것인지 정하기
     *
     * case 1:학생 로그인
     *  1. 학생 계정이 존재하지 않는 경우(studentRepository.findBySid(form.sid))
     *      1-1. username, password 로 인증 성공한 경우
     *          studentRepository.save(user),
     *          token 발급
     *      1-2. username, password 로 인증 실패하는 경우
     *          throw LoginFailedException
     *  2. 학생 계정이 존재하는 경우
     *      2-1. student의 isChecked가 false인 경우
     *          제안. isChecked가 false인 경우 관리자 시스템에서 학생 수동 추가한 경우로 설정,
     *          isChecked가 false 이면 관리자 시스템에서 수동 추가한 정보가 잘못 되었을 수 있으니 히즈넷 API로 조회한 데이터로 update 하기,
     *          token 발급
     *          TODO: 관리자 시스템에서 수동 추가한 학생 계정 처리 방식 논의(isChecked)
     *      2-2. student의 isChecked가 true인 경우
     *          학생 시스템에서 회원가입 해서 이미 계정이 존재하는 경우,
     *          token 발급
     *
     * case 2:관리자 로그인
     *  1. 관리자 계정이 존재하지 않는 경우(adminRepository.findByEmail(form.email) 혹은 직번 활용)
     *      1-1. username, password 로 인증 성공한 경우
     *          adminRepository.save(user)
     *          token 발급
     *      1-2. username, password 로 인증 실패하는 경우
     *          throw LoginFailedException
     *  2. 관리자 계정이 존재하는 경우
     *      token 발급
     *
     *  TODO: 관리자 회원가입 시 권한 설정 관련 논의(idea: 최고 관리자에게 이메일로 권한 요청 or 학교 API에서 직급 정보 조회해서 설정)
     * */
    fun studentLogin(form: LoginForm): String {
        val student = studentRepository.findBySid(form.uniqueId!!)
            .orElse(null)
        // 새로운 학생인 경우만 회원가입
        if (student == null) {
            // 학교 open API 호출해서 학생 정보 가져오기 -> TODO: 백엔드에서 호출할지 프론트에서 호출해서 정보 보내줄 지 결정
            // username, password에 해당하는 계정이 없는 경우 예외 발생
            val studentForm = StudentForm(
                sid = form.uniqueId,
                name = "이름",
                department = "전산전자공학부",
                major1 = "컴퓨터공학",
                major2 = "경영학",
                year = 3,
                semesterCount = 6,
                isChecked = true
            )
            val user = Student.createStudent(studentForm)
            studentRepository.save(user)
        }
        val token = createStudentToken(form)
//        redisTemplate.opsForValue().set("JWT_TOKEN:" + form.sid, token, tokenProvider.getExpiration(token))
        return token
    }

    fun adminLogin(form: LoginForm): String {
        // TODO: 히즈넷 로그인 API 호출 -> 로그인 실패 exception 처리하기
        val admin = adminRepository.findByAid(form.uniqueId!!)
            .orElse(null)
        // 새로운 관리자인 경우만 회원가입
        if (admin == null) {
            // 현재는 최고 관리자로 생성되도록 설정함.
            // 히즈넷 로그인 결과 데이터로 form 생성 예정.
            val adminForm = AdminForm(
                name = "이름",
                aid = form.uniqueId,
                level = 0
            )
            val user = Admin.createAdmin(adminForm)
            adminRepository.save(user)
        }
        adminRepository.findByAid(form.uniqueId!!).get()
            .let {
                val token = createAdminToken(form, it.level!!)
//                redisTemplate.opsForValue().set("JWT_TOKEN:" + form.email, token, tokenProvider.getExpiration(token))
                return token
            }
    }

    private fun createStudentToken(form: LoginForm): String {
        val authenticationToken = UsernamePasswordAuthenticationToken(form.uniqueId, form.password)
        SecurityContextHolder.getContext().authentication = authenticationToken
        return tokenProvider.createStudentToken(authenticationToken)
    }

    private fun createAdminToken(form: LoginForm, level: Int): String {
        val authenticationToken = UsernamePasswordAuthenticationToken(form.uniqueId, form.password)
        SecurityContextHolder.getContext().authentication = authenticationToken
        return tokenProvider.createAdminToken(authenticationToken, level)
    }

//    fun adminLogout() {
//        val admin = SecurityContextHolder.getContext().authentication.principal as Admin
//        if (redisTemplate.opsForValue().get("JWT_TOKEN:" + admin.email) != null) {
//            redisTemplate.delete("JWT_TOKEN:" + admin.email) // Token 삭제
//        }
//    }
//
//    fun studentLogout() {
//        val student = SecurityContextHolder.getContext().authentication.principal as Student
//        if (redisTemplate.opsForValue().get("JWT_TOKEN:" + student.sid) != null) {
//            redisTemplate.delete("JWT_TOKEN:" + student.sid) // Token 삭제
//        }
//    }
}
