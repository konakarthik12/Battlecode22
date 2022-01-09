package pathfinder2;

import battlecode.common.*;

class UnrolledPathfinder {
    static final int RUBBLE_CONSTANT = 1;
    static final int DIST_CONSTANT = 1;
    static MapLocation pathfind(RobotController rc, MapLocation dest) throws GameActionException {
        MapLocation m_location = rc.getLocation();
        int dest_dist = dest.distanceSquaredTo(m_location);
        MapLocation a_location = m_location.translate(-2, 2);
        int min_a_cost = 5000;
        MapLocation min_a_move = null;
        int a_rubble = 5000;
        if (!rc.isLocationOccupied(a_location)) a_rubble = rc.senseRubble(a_location) + 10;
        MapLocation b_location = m_location.translate(-1, 2);
        int min_b_cost = 5000;
        MapLocation min_b_move = null;
        int b_rubble = 5000;
        if (!rc.isLocationOccupied(b_location)) b_rubble = rc.senseRubble(b_location) + 10;
        MapLocation c_location = m_location.translate(0, 2);
        int min_c_cost = 5000;
        MapLocation min_c_move = null;
        int c_rubble = 5000;
        if (!rc.isLocationOccupied(c_location)) c_rubble = rc.senseRubble(c_location) + 10;
        MapLocation d_location = m_location.translate(1, 2);
        int min_d_cost = 5000;
        MapLocation min_d_move = null;
        int d_rubble = 5000;
        if (!rc.isLocationOccupied(d_location)) d_rubble = rc.senseRubble(d_location) + 10;
        MapLocation e_location = m_location.translate(2, 2);
        int min_e_cost = 5000;
        MapLocation min_e_move = null;
        int e_rubble = 5000;
        if (!rc.isLocationOccupied(e_location)) e_rubble = rc.senseRubble(e_location) + 10;
        MapLocation f_location = m_location.translate(-2, 1);
        int min_f_cost = 5000;
        MapLocation min_f_move = null;
        int f_rubble = 5000;
        if (!rc.isLocationOccupied(f_location)) f_rubble = rc.senseRubble(f_location) + 10;
        MapLocation g_location = m_location.translate(-1, 1);
        int g_path_cost = 5000;
        if (!rc.isLocationOccupied(g_location)) g_path_cost = rc.senseRubble(g_location) + 10;
        MapLocation h_location = m_location.translate(0, 1);
        int h_path_cost = 5000;
        if (!rc.isLocationOccupied(h_location)) h_path_cost = rc.senseRubble(h_location) + 10;
        MapLocation i_location = m_location.translate(1, 1);
        int i_path_cost = 5000;
        if (!rc.isLocationOccupied(i_location)) i_path_cost = rc.senseRubble(i_location) + 10;
        MapLocation j_location = m_location.translate(2, 1);
        int min_j_cost = 5000;
        MapLocation min_j_move = null;
        int j_rubble = 5000;
        if (!rc.isLocationOccupied(j_location)) j_rubble = rc.senseRubble(j_location) + 10;
        MapLocation k_location = m_location.translate(-2, 0);
        int min_k_cost = 5000;
        MapLocation min_k_move = null;
        int k_rubble = 5000;
        if (!rc.isLocationOccupied(k_location)) k_rubble = rc.senseRubble(k_location) + 10;
        MapLocation l_location = m_location.translate(-1, 0);
        int l_path_cost = 5000;
        if (!rc.isLocationOccupied(l_location)) l_path_cost = rc.senseRubble(l_location) + 10;
        MapLocation n_location = m_location.translate(1, 0);
        int n_path_cost = 5000;
        if (!rc.isLocationOccupied(n_location)) n_path_cost = rc.senseRubble(n_location) + 10;
        MapLocation o_location = m_location.translate(2, 0);
        int min_o_cost = 5000;
        MapLocation min_o_move = null;
        int o_rubble = 5000;
        if (!rc.isLocationOccupied(o_location)) o_rubble = rc.senseRubble(o_location) + 10;
        MapLocation p_location = m_location.translate(-2, -1);
        int min_p_cost = 5000;
        MapLocation min_p_move = null;
        int p_rubble = 5000;
        if (!rc.isLocationOccupied(p_location)) p_rubble = rc.senseRubble(p_location) + 10;
        MapLocation q_location = m_location.translate(-1, -1);
        int q_path_cost = 5000;
        if (!rc.isLocationOccupied(q_location)) q_path_cost = rc.senseRubble(q_location) + 10;
        MapLocation r_location = m_location.translate(0, -1);
        int r_path_cost = 5000;
        if (!rc.isLocationOccupied(r_location)) r_path_cost = rc.senseRubble(r_location) + 10;
        MapLocation s_location = m_location.translate(1, -1);
        int s_path_cost = 5000;
        if (!rc.isLocationOccupied(s_location)) s_path_cost = rc.senseRubble(s_location) + 10;
        MapLocation t_location = m_location.translate(2, -1);
        int min_t_cost = 5000;
        MapLocation min_t_move = null;
        int t_rubble = 5000;
        if (!rc.isLocationOccupied(t_location)) t_rubble = rc.senseRubble(t_location) + 10;
        MapLocation u_location = m_location.translate(-2, -2);
        int min_u_cost = 5000;
        MapLocation min_u_move = null;
        int u_rubble = 5000;
        if (!rc.isLocationOccupied(u_location)) u_rubble = rc.senseRubble(u_location) + 10;
        MapLocation v_location = m_location.translate(-1, -2);
        int min_v_cost = 5000;
        MapLocation min_v_move = null;
        int v_rubble = 5000;
        if (!rc.isLocationOccupied(v_location)) v_rubble = rc.senseRubble(v_location) + 10;
        MapLocation w_location = m_location.translate(0, -2);
        int min_w_cost = 5000;
        MapLocation min_w_move = null;
        int w_rubble = 5000;
        if (!rc.isLocationOccupied(w_location)) w_rubble = rc.senseRubble(w_location) + 10;
        MapLocation x_location = m_location.translate(1, -2);
        int min_x_cost = 5000;
        MapLocation min_x_move = null;
        int x_rubble = 5000;
        if (!rc.isLocationOccupied(x_location)) x_rubble = rc.senseRubble(x_location) + 10;
        MapLocation y_location = m_location.translate(2, -2);
        int min_y_cost = 5000;
        MapLocation min_y_move = null;
        int y_rubble = 5000;
        if (!rc.isLocationOccupied(y_location)) y_rubble = rc.senseRubble(y_location) + 10;
        int ie_path_cost = i_path_cost + e_rubble;
        if (ie_path_cost < min_e_cost) {
            min_e_cost = ie_path_cost;
            min_e_move = i_location;
        }
        int qu_path_cost = q_path_cost + u_rubble;
        if (qu_path_cost < min_u_cost) {
            min_u_cost = qu_path_cost;
            min_u_move = q_location;
        }
        int rv_path_cost = r_path_cost + v_rubble;
        if (rv_path_cost < min_v_cost) {
            min_v_cost = rv_path_cost;
            min_v_move = r_location;
        }
        int qv_path_cost = q_path_cost + v_rubble;
        if (qv_path_cost < min_v_cost) {
            min_v_cost = qv_path_cost;
            min_v_move = q_location;
        }
        int lp_path_cost = l_path_cost + p_rubble;
        if (lp_path_cost < min_p_cost) {
            min_p_cost = lp_path_cost;
            min_p_move = l_location;
        }
        int qp_path_cost = q_path_cost + p_rubble;
        if (qp_path_cost < min_p_cost) {
            min_p_cost = qp_path_cost;
            min_p_move = q_location;
        }
        int no_path_cost = n_path_cost + o_rubble;
        if (no_path_cost < min_o_cost) {
            min_o_cost = no_path_cost;
            min_o_move = n_location;
        }
        int io_path_cost = i_path_cost + o_rubble;
        if (io_path_cost < min_o_cost) {
            min_o_cost = io_path_cost;
            min_o_move = i_location;
        }
        int so_path_cost = s_path_cost + o_rubble;
        if (so_path_cost < min_o_cost) {
            min_o_cost = so_path_cost;
            min_o_move = s_location;
        }
        int hc_path_cost = h_path_cost + c_rubble;
        if (hc_path_cost < min_c_cost) {
            min_c_cost = hc_path_cost;
            min_c_move = h_location;
        }
        int ic_path_cost = i_path_cost + c_rubble;
        if (ic_path_cost < min_c_cost) {
            min_c_cost = ic_path_cost;
            min_c_move = i_location;
        }
        int gc_path_cost = g_path_cost + c_rubble;
        if (gc_path_cost < min_c_cost) {
            min_c_cost = gc_path_cost;
            min_c_move = g_location;
        }
        int nj_path_cost = n_path_cost + j_rubble;
        if (nj_path_cost < min_j_cost) {
            min_j_cost = nj_path_cost;
            min_j_move = n_location;
        }
        int ij_path_cost = i_path_cost + j_rubble;
        if (ij_path_cost < min_j_cost) {
            min_j_cost = ij_path_cost;
            min_j_move = i_location;
        }
        int hd_path_cost = h_path_cost + d_rubble;
        if (hd_path_cost < min_d_cost) {
            min_d_cost = hd_path_cost;
            min_d_move = h_location;
        }
        int id_path_cost = i_path_cost + d_rubble;
        if (id_path_cost < min_d_cost) {
            min_d_cost = id_path_cost;
            min_d_move = i_location;
        }
        int ga_path_cost = g_path_cost + a_rubble;
        if (ga_path_cost < min_a_cost) {
            min_a_cost = ga_path_cost;
            min_a_move = g_location;
        }
        int sy_path_cost = s_path_cost + y_rubble;
        if (sy_path_cost < min_y_cost) {
            min_y_cost = sy_path_cost;
            min_y_move = s_location;
        }
        int hb_path_cost = h_path_cost + b_rubble;
        if (hb_path_cost < min_b_cost) {
            min_b_cost = hb_path_cost;
            min_b_move = h_location;
        }
        int gb_path_cost = g_path_cost + b_rubble;
        if (gb_path_cost < min_b_cost) {
            min_b_cost = gb_path_cost;
            min_b_move = g_location;
        }
        int nt_path_cost = n_path_cost + t_rubble;
        if (nt_path_cost < min_t_cost) {
            min_t_cost = nt_path_cost;
            min_t_move = n_location;
        }
        int st_path_cost = s_path_cost + t_rubble;
        if (st_path_cost < min_t_cost) {
            min_t_cost = st_path_cost;
            min_t_move = s_location;
        }
        int lk_path_cost = l_path_cost + k_rubble;
        if (lk_path_cost < min_k_cost) {
            min_k_cost = lk_path_cost;
            min_k_move = l_location;
        }
        int gk_path_cost = g_path_cost + k_rubble;
        if (gk_path_cost < min_k_cost) {
            min_k_cost = gk_path_cost;
            min_k_move = g_location;
        }
        int qk_path_cost = q_path_cost + k_rubble;
        if (qk_path_cost < min_k_cost) {
            min_k_cost = qk_path_cost;
            min_k_move = q_location;
        }
        int rw_path_cost = r_path_cost + w_rubble;
        if (rw_path_cost < min_w_cost) {
            min_w_cost = rw_path_cost;
            min_w_move = r_location;
        }
        int sw_path_cost = s_path_cost + w_rubble;
        if (sw_path_cost < min_w_cost) {
            min_w_cost = sw_path_cost;
            min_w_move = s_location;
        }
        int qw_path_cost = q_path_cost + w_rubble;
        if (qw_path_cost < min_w_cost) {
            min_w_cost = qw_path_cost;
            min_w_move = q_location;
        }
        int lf_path_cost = l_path_cost + f_rubble;
        if (lf_path_cost < min_f_cost) {
            min_f_cost = lf_path_cost;
            min_f_move = l_location;
        }
        int gf_path_cost = g_path_cost + f_rubble;
        if (gf_path_cost < min_f_cost) {
            min_f_cost = gf_path_cost;
            min_f_move = g_location;
        }
        int rx_path_cost = r_path_cost + x_rubble;
        if (rx_path_cost < min_x_cost) {
            min_x_cost = rx_path_cost;
            min_x_move = r_location;
        }
        int sx_path_cost = s_path_cost + x_rubble;
        if (sx_path_cost < min_x_cost) {
            min_x_cost = sx_path_cost;
            min_x_move = s_location;
        }
        int nje_path_cost = nj_path_cost + e_rubble;
        if (nje_path_cost < min_e_cost) {
            min_e_cost = nje_path_cost;
            min_e_move = n_location;
        }
        int hde_path_cost = hd_path_cost + e_rubble;
        if (hde_path_cost < min_e_cost) {
            min_e_cost = hde_path_cost;
            min_e_move = h_location;
        }
        int lpu_path_cost = lp_path_cost + u_rubble;
        if (lpu_path_cost < min_u_cost) {
            min_u_cost = lpu_path_cost;
            min_u_move = l_location;
        }
        int rvu_path_cost = rv_path_cost + u_rubble;
        if (rvu_path_cost < min_u_cost) {
            min_u_cost = rvu_path_cost;
            min_u_move = r_location;
        }
        int lpv_path_cost = lp_path_cost + v_rubble;
        if (lpv_path_cost < min_v_cost) {
            min_v_cost = lpv_path_cost;
            min_v_move = l_location;
        }
        int swv_path_cost = sw_path_cost + v_rubble;
        if (swv_path_cost < min_v_cost) {
            min_v_cost = swv_path_cost;
            min_v_move = s_location;
        }
        int rvp_path_cost = rv_path_cost + p_rubble;
        if (rvp_path_cost < min_p_cost) {
            min_p_cost = rvp_path_cost;
            min_p_move = r_location;
        }
        int gkp_path_cost = gk_path_cost + p_rubble;
        if (gkp_path_cost < min_p_cost) {
            min_p_cost = gkp_path_cost;
            min_p_move = g_location;
        }
        int hdj_path_cost = hd_path_cost + j_rubble;
        if (hdj_path_cost < min_j_cost) {
            min_j_cost = hdj_path_cost;
            min_j_move = h_location;
        }
        int soj_path_cost = so_path_cost + j_rubble;
        if (soj_path_cost < min_j_cost) {
            min_j_cost = soj_path_cost;
            min_j_move = s_location;
        }
        int njd_path_cost = nj_path_cost + d_rubble;
        if (njd_path_cost < min_d_cost) {
            min_d_cost = njd_path_cost;
            min_d_move = n_location;
        }
        int gcd_path_cost = gc_path_cost + d_rubble;
        if (gcd_path_cost < min_d_cost) {
            min_d_cost = gcd_path_cost;
            min_d_move = g_location;
        }
        int lfa_path_cost = lf_path_cost + a_rubble;
        if (lfa_path_cost < min_a_cost) {
            min_a_cost = lfa_path_cost;
            min_a_move = l_location;
        }
        int hba_path_cost = hb_path_cost + a_rubble;
        if (hba_path_cost < min_a_cost) {
            min_a_cost = hba_path_cost;
            min_a_move = h_location;
        }
        int nty_path_cost = nt_path_cost + y_rubble;
        if (nty_path_cost < min_y_cost) {
            min_y_cost = nty_path_cost;
            min_y_move = n_location;
        }
        int rxy_path_cost = rx_path_cost + y_rubble;
        if (rxy_path_cost < min_y_cost) {
            min_y_cost = rxy_path_cost;
            min_y_move = r_location;
        }
        int lfb_path_cost = lf_path_cost + b_rubble;
        if (lfb_path_cost < min_b_cost) {
            min_b_cost = lfb_path_cost;
            min_b_move = l_location;
        }
        int icb_path_cost = ic_path_cost + b_rubble;
        if (icb_path_cost < min_b_cost) {
            min_b_cost = icb_path_cost;
            min_b_move = i_location;
        }
        int rxt_path_cost = rx_path_cost + t_rubble;
        if (rxt_path_cost < min_t_cost) {
            min_t_cost = rxt_path_cost;
            min_t_move = r_location;
        }
        int iot_path_cost = io_path_cost + t_rubble;
        if (iot_path_cost < min_t_cost) {
            min_t_cost = iot_path_cost;
            min_t_move = i_location;
        }
        int hbf_path_cost = hb_path_cost + f_rubble;
        if (hbf_path_cost < min_f_cost) {
            min_f_cost = hbf_path_cost;
            min_f_move = h_location;
        }
        int qkf_path_cost = qk_path_cost + f_rubble;
        if (qkf_path_cost < min_f_cost) {
            min_f_cost = qkf_path_cost;
            min_f_move = q_location;
        }
        int ntx_path_cost = nt_path_cost + x_rubble;
        if (ntx_path_cost < min_x_cost) {
            min_x_cost = ntx_path_cost;
            min_x_move = n_location;
        }
        int qwx_path_cost = qw_path_cost + x_rubble;
        if (qwx_path_cost < min_x_cost) {
            min_x_cost = qwx_path_cost;
            min_x_move = q_location;
        }
        int gcde_path_cost = gcd_path_cost + e_rubble;
        if (gcde_path_cost < min_e_cost) {
            min_e_cost = gcde_path_cost;
            min_e_move = g_location;
        }
        int soje_path_cost = soj_path_cost + e_rubble;
        if (soje_path_cost < min_e_cost) {
            min_e_cost = soje_path_cost;
            min_e_move = s_location;
        }
        int gkpu_path_cost = gkp_path_cost + u_rubble;
        if (gkpu_path_cost < min_u_cost) {
            min_u_cost = gkpu_path_cost;
            min_u_move = g_location;
        }
        int swvu_path_cost = swv_path_cost + u_rubble;
        if (swvu_path_cost < min_u_cost) {
            min_u_cost = swvu_path_cost;
            min_u_move = s_location;
        }
        int gkpv_path_cost = gkp_path_cost + v_rubble;
        if (gkpv_path_cost < min_v_cost) {
            min_v_cost = gkpv_path_cost;
            min_v_move = g_location;
        }
        int swvp_path_cost = swv_path_cost + p_rubble;
        if (swvp_path_cost < min_p_cost) {
            min_p_cost = swvp_path_cost;
            min_p_move = s_location;
        }
        int hdjo_path_cost = hdj_path_cost + o_rubble;
        if (hdjo_path_cost < min_o_cost) {
            min_o_cost = hdjo_path_cost;
            min_o_move = h_location;
        }
        int rxto_path_cost = rxt_path_cost + o_rubble;
        if (rxto_path_cost < min_o_cost) {
            min_o_cost = rxto_path_cost;
            min_o_move = r_location;
        }
        int njdc_path_cost = njd_path_cost + c_rubble;
        if (njdc_path_cost < min_c_cost) {
            min_c_cost = njdc_path_cost;
            min_c_move = n_location;
        }
        int lfbc_path_cost = lfb_path_cost + c_rubble;
        if (lfbc_path_cost < min_c_cost) {
            min_c_cost = lfbc_path_cost;
            min_c_move = l_location;
        }
        int gcdj_path_cost = gcd_path_cost + j_rubble;
        if (gcdj_path_cost < min_j_cost) {
            min_j_cost = gcdj_path_cost;
            min_j_move = g_location;
        }
        int sojd_path_cost = soj_path_cost + d_rubble;
        if (sojd_path_cost < min_d_cost) {
            min_d_cost = sojd_path_cost;
            min_d_move = s_location;
        }
        int icba_path_cost = icb_path_cost + a_rubble;
        if (icba_path_cost < min_a_cost) {
            min_a_cost = icba_path_cost;
            min_a_move = i_location;
        }
        int qkfa_path_cost = qkf_path_cost + a_rubble;
        if (qkfa_path_cost < min_a_cost) {
            min_a_cost = qkfa_path_cost;
            min_a_move = q_location;
        }
        int ioty_path_cost = iot_path_cost + y_rubble;
        if (ioty_path_cost < min_y_cost) {
            min_y_cost = ioty_path_cost;
            min_y_move = i_location;
        }
        int qwxy_path_cost = qwx_path_cost + y_rubble;
        if (qwxy_path_cost < min_y_cost) {
            min_y_cost = qwxy_path_cost;
            min_y_move = q_location;
        }
        int qkfb_path_cost = qkf_path_cost + b_rubble;
        if (qkfb_path_cost < min_b_cost) {
            min_b_cost = qkfb_path_cost;
            min_b_move = q_location;
        }
        int qwxt_path_cost = qwx_path_cost + t_rubble;
        if (qwxt_path_cost < min_t_cost) {
            min_t_cost = qwxt_path_cost;
            min_t_move = q_location;
        }
        int hbfk_path_cost = hbf_path_cost + k_rubble;
        if (hbfk_path_cost < min_k_cost) {
            min_k_cost = hbfk_path_cost;
            min_k_move = h_location;
        }
        int rvpk_path_cost = rvp_path_cost + k_rubble;
        if (rvpk_path_cost < min_k_cost) {
            min_k_cost = rvpk_path_cost;
            min_k_move = r_location;
        }
        int ntxw_path_cost = ntx_path_cost + w_rubble;
        if (ntxw_path_cost < min_w_cost) {
            min_w_cost = ntxw_path_cost;
            min_w_move = n_location;
        }
        int lpvw_path_cost = lpv_path_cost + w_rubble;
        if (lpvw_path_cost < min_w_cost) {
            min_w_cost = lpvw_path_cost;
            min_w_move = l_location;
        }
        int icbf_path_cost = icb_path_cost + f_rubble;
        if (icbf_path_cost < min_f_cost) {
            min_f_cost = icbf_path_cost;
            min_f_move = i_location;
        }
        int iotx_path_cost = iot_path_cost + x_rubble;
        if (iotx_path_cost < min_x_cost) {
            min_x_cost = iotx_path_cost;
            min_x_move = i_location;
        }
        MapLocation best_target = null;
        int best_min = 6000;
        int temp;
        temp = min_a_cost * RUBBLE_CONSTANT + (dest.distanceSquaredTo(a_location)) * DIST_CONSTANT;
        if (dest.distanceSquaredTo(a_location) <= dest_dist && temp < best_min) {
            rc.setIndicatorString(a_location.toString());
            best_target = min_a_move;
            best_min = temp;
        }
        temp = min_b_cost * RUBBLE_CONSTANT + (dest.distanceSquaredTo(b_location)) * DIST_CONSTANT;
        if (dest.distanceSquaredTo(b_location) <= dest_dist && temp < best_min) {
            rc.setIndicatorString(b_location.toString());
            best_target = min_b_move;
            best_min = temp;
        }
        temp = min_c_cost * RUBBLE_CONSTANT + (dest.distanceSquaredTo(c_location)) * DIST_CONSTANT;
        if (dest.distanceSquaredTo(c_location) <= dest_dist && temp < best_min) {
            rc.setIndicatorString(c_location.toString());
            best_target = min_c_move;
            best_min = temp;
        }
        temp = min_d_cost * RUBBLE_CONSTANT + (dest.distanceSquaredTo(d_location)) * DIST_CONSTANT;
        if (dest.distanceSquaredTo(d_location) <= dest_dist && temp < best_min) {
            rc.setIndicatorString(d_location.toString());
            best_target = min_d_move;
            best_min = temp;
        }
        temp = min_e_cost * RUBBLE_CONSTANT + (dest.distanceSquaredTo(e_location)) * DIST_CONSTANT;
        if (dest.distanceSquaredTo(e_location) <= dest_dist && temp < best_min) {
            rc.setIndicatorString(e_location.toString());
            best_target = min_e_move;
            best_min = temp;
        }
        temp = min_f_cost * RUBBLE_CONSTANT + (dest.distanceSquaredTo(f_location)) * DIST_CONSTANT;
        if (dest.distanceSquaredTo(f_location) <= dest_dist && temp < best_min) {
            rc.setIndicatorString(f_location.toString());
            best_target = min_f_move;
            best_min = temp;
        }
        temp = min_j_cost * RUBBLE_CONSTANT + (dest.distanceSquaredTo(j_location)) * DIST_CONSTANT;
        if (dest.distanceSquaredTo(j_location) <= dest_dist && temp < best_min) {
            rc.setIndicatorString(j_location.toString());
            best_target = min_j_move;
            best_min = temp;
        }
        temp = min_k_cost * RUBBLE_CONSTANT + (dest.distanceSquaredTo(k_location)) * DIST_CONSTANT;
        if (dest.distanceSquaredTo(k_location) <= dest_dist && temp < best_min) {
            rc.setIndicatorString(k_location.toString());
            best_target = min_k_move;
            best_min = temp;
        }
        temp = min_o_cost * RUBBLE_CONSTANT + (dest.distanceSquaredTo(o_location)) * DIST_CONSTANT;
        if (dest.distanceSquaredTo(o_location) <= dest_dist && temp < best_min) {
            rc.setIndicatorString(o_location.toString());
            best_target = min_o_move;
            best_min = temp;
        }
        temp = min_p_cost * RUBBLE_CONSTANT + (dest.distanceSquaredTo(p_location)) * DIST_CONSTANT;
        if (dest.distanceSquaredTo(p_location) <= dest_dist && temp < best_min) {
            rc.setIndicatorString(p_location.toString());
            best_target = min_p_move;
            best_min = temp;
        }
        temp = min_t_cost * RUBBLE_CONSTANT + (dest.distanceSquaredTo(t_location)) * DIST_CONSTANT;
        if (dest.distanceSquaredTo(t_location) <= dest_dist && temp < best_min) {
            rc.setIndicatorString(t_location.toString());
            best_target = min_t_move;
            best_min = temp;
        }
        temp = min_u_cost * RUBBLE_CONSTANT + (dest.distanceSquaredTo(u_location)) * DIST_CONSTANT;
        if (dest.distanceSquaredTo(u_location) <= dest_dist && temp < best_min) {
            rc.setIndicatorString(u_location.toString());
            best_target = min_u_move;
            best_min = temp;
        }
        temp = min_v_cost * RUBBLE_CONSTANT + (dest.distanceSquaredTo(v_location)) * DIST_CONSTANT;
        if (dest.distanceSquaredTo(v_location) <= dest_dist && temp < best_min) {
            rc.setIndicatorString(v_location.toString());
            best_target = min_v_move;
            best_min = temp;
        }
        temp = min_w_cost * RUBBLE_CONSTANT + (dest.distanceSquaredTo(w_location)) * DIST_CONSTANT;
        if (dest.distanceSquaredTo(w_location) <= dest_dist && temp < best_min) {
            rc.setIndicatorString(w_location.toString());
            best_target = min_w_move;
            best_min = temp;
        }
        temp = min_x_cost * RUBBLE_CONSTANT + (dest.distanceSquaredTo(x_location)) * DIST_CONSTANT;
        if (dest.distanceSquaredTo(x_location) <= dest_dist && temp < best_min) {
            rc.setIndicatorString(x_location.toString());
            best_target = min_x_move;
            best_min = temp;
        }
        temp = min_y_cost * RUBBLE_CONSTANT + (dest.distanceSquaredTo(y_location)) * DIST_CONSTANT;
        if (dest.distanceSquaredTo(y_location) <= dest_dist && temp < best_min) {
            rc.setIndicatorString(y_location.toString());
            best_target = min_y_move;
            best_min = temp;
        }
        return best_target;
    }
}