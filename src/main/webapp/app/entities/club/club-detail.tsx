import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './club.reducer';

export const ClubDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const clubEntity = useAppSelector(state => state.club.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="clubDetailsHeading">
          <Translate contentKey="isimmManagerApp.club.detail.title">Club</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{clubEntity.id}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="isimmManagerApp.club.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{clubEntity.nom}</dd>
          <dt>
            <span id="pageFB">
              <Translate contentKey="isimmManagerApp.club.pageFB">Page FB</Translate>
            </span>
          </dt>
          <dd>{clubEntity.pageFB}</dd>
          <dt>
            <span id="pageIg">
              <Translate contentKey="isimmManagerApp.club.pageIg">Page Ig</Translate>
            </span>
          </dt>
          <dd>{clubEntity.pageIg}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="isimmManagerApp.club.email">Email</Translate>
            </span>
          </dt>
          <dd>{clubEntity.email}</dd>
        </dl>
        <Button tag={Link} to="/club" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/club/${clubEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClubDetail;
